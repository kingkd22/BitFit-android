package com.example.bitfit

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.bitfit.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val appDb = (requireActivity().application as BitFitApplication).db
                appDb.foodDao().getAll().collect { foods ->
                    if (foods.isEmpty()) {
                        binding.tvAverageCaloriesValue.text = "0"
                        binding.tvMinCaloriesValue.text = "0"
                        binding.tvMaxCaloriesValue.text = "0"
                        binding.tvTotalItemsValue.text = "0"
                    } else {
                        val caloriesList = foods.map { it.calorieCount }
                        val avg = caloriesList.average().roundToInt()
                        val min = caloriesList.minOrNull() ?: 0
                        val max = caloriesList.maxOrNull() ?: 0

                        binding.tvAverageCaloriesValue.text = avg.toString()
                        binding.tvMinCaloriesValue.text = min.toString()
                        binding.tvMaxCaloriesValue.text = max.toString()
                        binding.tvTotalItemsValue.text = foods.size.toString()
                    }
                    updateChart(foods)
                }
            }
        }
    }

    private fun updateChart(foods: List<FoodEntity>) {
        if (foods.isEmpty()) {
            binding.lineChart.clear()
            binding.lineChart.setNoDataText("No food items logged yet")
            binding.lineChart.invalidate()
            return
        }

        val chronologicallySorted = foods.reversed()
        val entries = chronologicallySorted.mapIndexed { index, food ->
            Entry((index + 1).toFloat(), food.calorieCount.toFloat())
        }

        val dataSet = LineDataSet(entries, "Calories").apply {
            color = Color.parseColor("#6200EE")
            setCircleColor(Color.parseColor("#3700B3"))
            lineWidth = 2.5f
            circleRadius = 5f
            setDrawCircleHole(true)
            valueTextSize = 10f
            setDrawFilled(true)
            fillColor = Color.parseColor("#BB86FC")
            fillAlpha = 60
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        binding.lineChart.apply {
            data = LineData(dataSet)
            description.isEnabled = false
            legend.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f
            axisRight.isEnabled = false
            animateX(500)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
