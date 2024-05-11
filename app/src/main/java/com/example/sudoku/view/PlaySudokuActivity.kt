package com.example.sudoku.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sudoku.R
import com.example.sudoku.game.Cell
import com.example.sudoku.view.custom.SudokuBoardView
import com.example.sudoku.viewmodel.PlaySudokuViewModel
import com.google.android.material.snackbar.Snackbar



class PlaySudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {

    private lateinit var viewModel: PlaySudokuViewModel
    private lateinit var sudokuBoardView: SudokuBoardView
    private lateinit var numberButtons: List<Button>
    private lateinit var notesButton: ImageButton
    private lateinit var chronometer: Chronometer
    private lateinit var timeHandler: Handler
    private var timeElapsed: Long = 0
    private var startTime: Long = 0
    private var previousLowestTime: Long = Long.MAX_VALUE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.playsudoku_activity)

        sudokuBoardView = findViewById(R.id.sudokuBoardView)
        sudokuBoardView.registerListener(this)

        viewModel = ViewModelProvider(this)[PlaySudokuViewModel::class.java]
        viewModel.sudokuGame.selectedCellLiveData.observe(this, Observer { updateSelectedCellUI(it) })
        viewModel.sudokuGame.cellsLiveData.observe(this, Observer { updateCells(it) })
        viewModel.sudokuGame.isTakingNotesLiveData.observe(this, Observer { updateNoteTakingUI(it) })
        viewModel.sudokuGame.highlightedKeysLiveData.observe(this, Observer { updateHighlightedKeys(it) })


        chronometer = findViewById(R.id.chronometer)

        val oneButton = findViewById<Button>(R.id.oneButton)
        val twoButton = findViewById<Button>(R.id.twoButton)
        val threeButton = findViewById<Button>(R.id.threeButton)
        val fourButton = findViewById<Button>(R.id.fourButton)
        val fiveButton = findViewById<Button>(R.id.fiveButton)
        val sixButton = findViewById<Button>(R.id.sixButton)
        val sevenButton = findViewById<Button>(R.id.sevenButton)
        val eightButton = findViewById<Button>(R.id.eightButton)
        val nineButton = findViewById<Button>(R.id.nineButton)

        val solveButton = findViewById<Button>(R.id.solveButton)
        val quitBtn = findViewById<ImageButton>(R.id.quitBtn)

        notesButton = findViewById<ImageButton>(R.id.notesButton)

         numberButtons = listOf(oneButton, twoButton, threeButton, fourButton, fiveButton, sixButton,
            sevenButton, eightButton, nineButton)


        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener { viewModel.sudokuGame.handleInput(index + 1) }
        }


        notesButton.setOnClickListener{viewModel.sudokuGame.changeNoteTakingState()}



        solveButton.setOnClickListener {
            val isSolved = viewModel.sudokuGame.isGameSolved()
            if (isSolved) {
                verifySolution()
                stopTimer()
            } else {

                val snackbar = Snackbar.make(findViewById(android.R.id.content), "The puzzle is not yet solved.", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }

        quitBtn.setOnClickListener{
            val intent = Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }

        startTimer()

    }


    private fun startTimer() {
        timeHandler = Handler()
        timeHandler.postDelayed(updateTimeRunnable, 1000)
        chronometer.base = SystemClock.elapsedRealtime()
        startTime = System.currentTimeMillis()
        chronometer.start()
    }

    private fun stopTimer() {
        timeHandler.removeCallbacks(updateTimeRunnable)
        chronometer.stop()
    }

    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            timeElapsed += 1000
            timeHandler.postDelayed(this, 1000)
        }
    }

    private fun updateCells(cells: List<Cell>?) = cells?.let {
        sudokuBoardView.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        sudokuBoardView.updateSelectedCellUI(cell.first, cell.second)
    }

    private fun updateNoteTakingUI(isNoteTaking: Boolean?) = isNoteTaking?.let {
        if (it) {
            notesButton.setBackgroundColor(ContextCompat.getColor(this, R.color.light_green))
        } else {
            notesButton.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))
        }

    }

    private fun updateHighlightedKeys(set: Set<Int>?) = set?.let {
        numberButtons.forEachIndexed { index, button ->
            val color = if (set.contains(index + 1)) ContextCompat.getColor(this, R.color.light_green) else ContextCompat.getColor(this, R.color.light_grey)
            button.setBackgroundColor(color)
        }

    }

    private fun verifySolution() {
        val isSolved = viewModel.sudokuGame.isGameSolved()
        val message = if (isSolved) "Congratulations! You have solved the puzzle." else "The puzzle is not yet solved."
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        snackbar.show()

        if (isSolved) {
            storeSolvedTime(startTime)

            val handler = Handler()
            handler.postDelayed({
                val intent = Intent(this@PlaySudokuActivity, MainActivity2::class.java)
                startActivity(intent)
            }, 2000)
        }
    }

    private fun storeSolvedTime(startTime: Long) {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - startTime

        val sharedPreferences = getSharedPreferences("SudokuPrefs", Context.MODE_PRIVATE)


        val lowestTimeKey = when (globalDifficulty) {
            "easy" -> "lowest_time_easy"
            "hard" -> "lowest_time_hard"
            else -> throw IllegalArgumentException("Invalid difficulty level")
        }
        var previousLowestTime = sharedPreferences.getLong(lowestTimeKey, Long.MAX_VALUE)
        if (elapsedTime < previousLowestTime) {
            sharedPreferences.edit().putLong(lowestTimeKey, elapsedTime).apply()
        }
    }



    override fun onCellTouched(row: Int, col: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }


}
