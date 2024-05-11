package com.example.sudoku.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sudoku.R

var globalDifficulty: String = ""

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val easyBtn = findViewById<LinearLayout>(R.id.easyBtn)
        val hardBtn = findViewById<LinearLayout>(R.id.hardBtn)
//        val easyResetBtn = findViewById<Button>(R.id.resetEasyHighScore)
//        val hardResetBtn = findViewById<Button>(R.id.resetHardHighScore)
        val instructionsBtn = findViewById<LinearLayout>(R.id.instructions_Btn)
        val backbtn = findViewById<ImageButton>(R.id.backBtn2)

        // Find the TextView by its id
        val lowestTimeEasyTextView = findViewById<TextView>(R.id.lowestTimeEasy)
        val lowestTimeHardTextView = findViewById<TextView>(R.id.lowestTimeHard)
        
        easyBtn.setOnClickListener {
            val intent = Intent(this,PlaySudokuActivity::class.java)
            globalDifficulty = "easy"
            startActivity(intent)
        }

        hardBtn.setOnClickListener {
            val intent = Intent(this,PlaySudokuActivity::class.java)
            globalDifficulty = "hard"
            startActivity(intent)
        }

        backbtn.setOnClickListener {
            val intent = Intent(this@MainActivity2,MainActivity::class.java)
            startActivity(intent)
        }



//        easyResetBtn.setOnClickListener {
//            resetTimeValues("easy")
//            lowestTimeEasyTextView.text = formatTime(Long.MAX_VALUE)
//        }
//
//        hardResetBtn.setOnClickListener {
//            resetTimeValues("hard")
//            lowestTimeHardTextView.text = formatTime(Long.MAX_VALUE)
//        }

        instructionsBtn.setOnClickListener {
            val intent = Intent(this@MainActivity2,Instructions::class.java)
            startActivity(intent)
        }

        val lowestTimeEasy = getLowestTime("easy")
        val lowestTimeHard = getLowestTime("hard")



        // Set the lowest time as the text of the TextView
        lowestTimeEasyTextView.text = formatTime(lowestTimeEasy)
        lowestTimeHardTextView.text = formatTime(lowestTimeHard)


    }

    private fun formatTime(milliseconds: Long): String {
        val minutes = milliseconds / 60000
        val seconds = (milliseconds % 60000) / 1000
        return String.format("%02d min:%02d sec", minutes, seconds)
    }

    private fun getLowestTime(difficulty: String): Long {
        val sharedPreferences = getSharedPreferences("SudokuPrefs", Context.MODE_PRIVATE)
        val lowestTimeKey = when (difficulty) {
            "easy" -> "lowest_time_easy"
            "hard" -> "lowest_time_hard"
            else -> throw IllegalArgumentException("Invalid difficulty level")
        }
        return sharedPreferences.getLong(lowestTimeKey, Long.MAX_VALUE)
    }
//
//    private fun resetTimeValues(difficulty: String){
//        val sharedPreferences = getSharedPreferences("SudokuPrefs", Context.MODE_PRIVATE)
//        val lowestTimeKey = when (difficulty) {
//            "easy" -> "lowest_time_easy"
//            "hard" -> "lowest_time_hard"
//            else -> throw IllegalArgumentException("Invalid difficulty level")
//        }
//
//         sharedPreferences.edit().putLong(lowestTimeKey,Long.MAX_VALUE).apply()
//    }
}