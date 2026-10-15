package com.example.bitfit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bitfit.databinding.ItemFoodBinding

class FoodAdapter(
    private var foods: List<FoodEntity>
) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foods[position]
        holder.binding.tvFoodName.text = food.name
        val caloriesDisplay = if (food.calories.lowercase().contains("cal")) {
            food.calories
        } else {
            "${food.calories} Calories"
        }
        holder.binding.tvCalories.text = caloriesDisplay
    }

    override fun getItemCount(): Int = foods.size

    fun updateData(newFoods: List<FoodEntity>) {
        foods = newFoods
        notifyDataSetChanged()
    }
}
