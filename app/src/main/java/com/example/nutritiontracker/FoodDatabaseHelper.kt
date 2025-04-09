package com.example.nutritiontracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FoodDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "NutritionTracker.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_FOODS = "foods"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_CALORIES = "calories"
        private const val COLUMN_PROTEIN = "protein"
        private const val COLUMN_CARBS = "carbs"
        private const val COLUMN_FAT = "fat"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_FOODS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_CALORIES INTEGER NOT NULL,
                $COLUMN_PROTEIN REAL NOT NULL,
                $COLUMN_CARBS REAL NOT NULL,
                $COLUMN_FAT REAL NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)

        // Insert some sample data
        insertSampleFoods(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FOODS")
        onCreate(db)
    }

    private fun insertSampleFoods(db: SQLiteDatabase) {
        val sampleFoods = listOf(
            FoodItem("Apple", 95, 0.5, 25.0, 0.3),
            FoodItem("Banana", 105, 1.3, 27.0, 0.4),
            FoodItem("Chicken Breast", 165, 31.0, 0.0, 3.6),
            FoodItem("Rice (1 cup)", 205, 4.3, 45.0, 0.4)
        )

        sampleFoods.forEach { food ->
            val values = ContentValues().apply {
                put(COLUMN_NAME, food.name)
                put(COLUMN_CALORIES, food.calories)
                put(COLUMN_PROTEIN, food.protein)
                put(COLUMN_CARBS, food.carbs)
                put(COLUMN_FAT, food.fat)
            }
            db.insert(TABLE_FOODS, null, values)
        }
    }

    fun searchFoods(query: String): List<FoodItem> {
        val foods = mutableListOf<FoodItem>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_FOODS,
            null,
            "$COLUMN_NAME LIKE ?",
            arrayOf("%$query%"),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val food = FoodItem(
                    getString(getColumnIndexOrThrow(COLUMN_NAME)),
                    getInt(getColumnIndexOrThrow(COLUMN_CALORIES)),
                    getDouble(getColumnIndexOrThrow(COLUMN_PROTEIN)),
                    getDouble(getColumnIndexOrThrow(COLUMN_CARBS)),
                    getDouble(getColumnIndexOrThrow(COLUMN_FAT))
                )
                foods.add(food)
            }
            close()
        }
        return foods
    }

    fun addFood(food: FoodItem): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, food.name)
            put(COLUMN_CALORIES, food.calories)
            put(COLUMN_PROTEIN, food.protein)
            put(COLUMN_CARBS, food.carbs)
            put(COLUMN_FAT, food.fat)
        }
        return db.insert(TABLE_FOODS, null, values)
    }
}
