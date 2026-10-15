package com.example.bitfit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.bitfit.databinding.FragmentDashboardBinding
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
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
