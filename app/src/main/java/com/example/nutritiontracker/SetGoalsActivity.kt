package com.example.nutritiontracker

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class SetGoalsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_goals)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val calorieGoalEditText = findViewById<EditText>(R.id.calorieGoalEditText)
        val proteinGoalEditText = findViewById<EditText>(R.id.proteinGoalEditText)
        val carbsGoalEditText = findViewById<EditText>(R.id.carbsGoalEditText)
        val fatGoalEditText = findViewById<EditText>(R.id.fatGoalEditText)
        val saveButton = findViewById<Button>(R.id.saveGoalsButton)

        // Load existing goals if they exist
        calorieGoalEditText.setText(sharedPreferences.getInt("calorie_goal", 2000).toString())
        proteinGoalEditText.setText(sharedPreferences.getFloat("protein_goal", 50f).toString())
        carbsGoalEditText.setText(sharedPreferences.getFloat("carbs_goal", 250f).toString())
        fatGoalEditText.setText(sharedPreferences.getFloat("fat_goal", 70f).toString())

        saveButton.setOnClickListener {
            try {
                val calorieGoal = calorieGoalEditText.text.toString().toInt()
                val proteinGoal = proteinGoalEditText.text.toString().toFloat()
                val carbsGoal = carbsGoalEditText.text.toString().toFloat()
                val fatGoal = fatGoalEditText.text.toString().toFloat()

                with(sharedPreferences.edit()) {
                    putInt("calorie_goal", calorieGoal)
                    putFloat("protein_goal", proteinGoal)
                    putFloat("carbs_goal", carbsGoal)
                    putFloat("fat_goal", fatGoal)
                    apply()
                }

                Toast.makeText(this, R.string.goals_saved, Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
