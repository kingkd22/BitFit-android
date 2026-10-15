package com.example.bitfit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.bitfit.databinding.ActivityDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnRecordFood.setOnClickListener {
            val name = binding.etFoodName.text.toString().trim()
            val calories = binding.etCalories.text.toString().trim()

            if (name.isEmpty() || calories.isEmpty()) {
                Toast.makeText(this, "Please enter both food name and calories", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val food = FoodEntity(name = name, calories = calories)

            lifecycleScope.launch(Dispatchers.IO) {
                (application as BitFitApplication).db.foodDao().insert(food)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailActivity, "Food recorded!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
