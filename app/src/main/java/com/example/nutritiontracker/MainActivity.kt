package com.example.nutritiontracker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var foodSearchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var foodResultsListView: ListView
    private lateinit var calorieSummaryTextView: TextView
    private lateinit var dbHelper: FoodDatabaseHelper
    private var totalCalories = 0
    private var totalProtein = 0.0
    private var totalCarbs = 0.0
    private var totalFat = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = FoodDatabaseHelper(this)

        // Initialize views
        foodSearchEditText = findViewById(R.id.foodSearchEditText)
        searchButton = findViewById(R.id.searchButton)
        foodResultsListView = findViewById(R.id.foodResultsListView)
        calorieSummaryTextView = findViewById(R.id.calorieSummaryTextView)

        // Set up search button click listener
        searchButton.setOnClickListener {
            val query = foodSearchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                searchFoodItems(query)
            } else {
                Toast.makeText(this, "Please enter a food name", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up list item click listener
        foodResultsListView.setOnItemClickListener { _, _, position, _ ->
            val selectedFood = foodResultsListView.adapter.getItem(position) as FoodItem
            addToDailyNutrition(selectedFood)
        }

        // Set up add food button click listener
        val addFoodButton = findViewById<Button>(R.id.addFoodButton)
        addFoodButton.setOnClickListener {
            val intent = Intent(this, AddFoodActivity::class.java)
            startActivity(intent)
        }

        // Set up set goals button click listener
        val setGoalsButton = findViewById<Button>(R.id.setGoalsButton)
        setGoalsButton.setOnClickListener {
            val intent = Intent(this, SetGoalsActivity::class.java)
            startActivity(intent)
        }

        updateNutritionSummary()
    }

    private fun searchFoodItems(query: String) {
        val results = dbHelper.searchFoods(query)
        if (results.isNotEmpty()) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                results
            )
            foodResultsListView.adapter = adapter
        } else {
            Toast.makeText(this, "No food items found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToDailyNutrition(food: FoodItem) {
        totalCalories += food.calories
        totalProtein += food.protein
        totalCarbs += food.carbs
        totalFat += food.fat
        updateNutritionSummary()
        Toast.makeText(this, "Added ${food.name} to daily nutrition", Toast.LENGTH_SHORT).show()
    }

    private fun updateNutritionSummary() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val calorieGoal = sharedPreferences.getInt("calorie_goal", 2000)
        
        val progressPercent = if (calorieGoal > 0) {
            (totalCalories.toFloat() / calorieGoal * 100).coerceAtMost(100f)
        } else {
            0f
        }
        
        val summary = getString(
            R.string.progress,
            totalCalories,
            calorieGoal,
            progressPercent
        )
        calorieSummaryTextView.text = summary
    }

}

data class FoodItem(
    val name: String,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double
) {
    override fun toString(): String {
        return "$name - $calories cal (P:${protein}g, C:${carbs}g, F:${fat}g)"
    }
}
