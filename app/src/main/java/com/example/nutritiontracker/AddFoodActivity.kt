package com.example.nutritiontracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddFoodActivity : AppCompatActivity() {
    private lateinit var dbHelper: FoodDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        dbHelper = FoodDatabaseHelper(this)

        val foodNameEditText = findViewById<EditText>(R.id.foodNameEditText)
        val caloriesEditText = findViewById<EditText>(R.id.caloriesEditText)
        val proteinEditText = findViewById<EditText>(R.id.proteinEditText)
        val carbsEditText = findViewById<EditText>(R.id.carbsEditText)
        val fatEditText = findViewById<EditText>(R.id.fatEditText)
        val saveButton = findViewById<Button>(R.id.saveFoodButton)

        saveButton.setOnClickListener {
            val name = foodNameEditText.text.toString().trim()
            val calories = caloriesEditText.text.toString().trim()
            val protein = proteinEditText.text.toString().trim()
            val carbs = carbsEditText.text.toString().trim()
            val fat = fatEditText.text.toString().trim()

            if (name.isEmpty() || calories.isEmpty() || protein.isEmpty() || carbs.isEmpty() || fat.isEmpty()) {
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val foodItem = FoodItem(
                name,
                calories.toInt(),
                protein.toDouble(),
                carbs.toDouble(),
                fat.toDouble()
            )

            dbHelper.addFood(foodItem)
            Toast.makeText(this, R.string.food_added_success, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
