package com.example.bitfit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitfit.databinding.FragmentLogBinding
import kotlinx.coroutines.launch

class LogFragment : Fragment() {

    private var _binding: FragmentLogBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FoodAdapter(emptyList())
        binding.rvFoods.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFoods.adapter = adapter

        binding.btnAddFood.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            startActivity(intent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val appDb = (requireActivity().application as BitFitApplication).db
                appDb.foodDao().getAll().collect { foods ->
                    adapter.updateData(foods)
                    if (foods.isEmpty()) {
                        binding.tvEmptyLog.visibility = View.VISIBLE
                        binding.rvFoods.visibility = View.GONE
                    } else {
                        binding.tvEmptyLog.visibility = View.GONE
                        binding.rvFoods.visibility = View.VISIBLE
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
