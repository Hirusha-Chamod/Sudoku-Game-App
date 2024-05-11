package com.example.sudoku.game

import androidx.lifecycle.MutableLiveData
import com.example.sudoku.view.globalDifficulty

class SudokuGame {


    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()
    val isTakingNotesLiveData = MutableLiveData<Boolean>()
    val highlightedKeysLiveData = MutableLiveData<Set<Int>>()

    private var selectedRow = -1
    private var selectedCol = -1
    private var isTakingNotes = false

    private val board: Board




    init {
        val cells = mutableListOf<Cell>()


        val easyTemplate = listOf(
            listOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
            listOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
            listOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
            listOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
            listOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
            listOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
            listOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
            listOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
            listOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
        )


        val hardTemplate = listOf(
            listOf(8, 0, 0, 0, 0, 0, 0, 0, 0),
            listOf(0, 0, 3, 6, 0, 0, 0, 0, 0),
            listOf(0, 7, 0, 0, 9, 0, 2, 0, 0),
            listOf(0, 5, 0, 0, 0, 7, 0, 0, 0),
            listOf(0, 0, 0, 0, 4, 5, 7, 0, 0),
            listOf(0, 0, 0, 1, 0, 0, 0, 3, 0),
            listOf(0, 0, 1, 0, 0, 0, 0, 6, 8),
            listOf(0, 0, 8, 5, 0, 0, 0, 1, 0),
            listOf(0, 9, 0, 0, 0, 0, 4, 0, 0)
        )

        val selectedTemplate = when (globalDifficulty) {
            "easy" -> easyTemplate
            "hard" -> hardTemplate
            else -> throw IllegalArgumentException("Invalid difficulty level")
        }

        // Populate the cells list using the selected template
        for (i in selectedTemplate.indices) {
            for (j in selectedTemplate[i].indices) {
                val value = selectedTemplate[i][j]
                val isStartingCell = value != 0
                val cell = Cell(i, j, value)
                cell.isStartingCell = isStartingCell
                cells.add(cell)
            }
        }
        board = Board(9, cells)

        // Post initial values to LiveData
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(board.cells)
        isTakingNotesLiveData.postValue(isTakingNotes)
    }

    fun handleInput(number: Int) {
        if (selectedRow == -1 || selectedCol == -1) return
        val cell = board.getCell(selectedRow, selectedCol)
        if (cell.isStartingCell) return

        if (isTakingNotes) {
            if (cell.notes.contains(number)) {
                cell.notes.remove(number)
            } else {
                cell.notes.add(number)
            }
            highlightedKeysLiveData.postValue(cell.notes)
        } else {
            cell.value = number
        }
        cellsLiveData.postValue(board.cells)
    }


    fun updateSelectedCell(row: Int, col: Int) {
        val cell = board.getCell(row, col)
        if (!cell.isStartingCell) {
            selectedRow = row
            selectedCol = col
            selectedCellLiveData.postValue(Pair(row, col))

            if (isTakingNotes) {
                highlightedKeysLiveData.postValue(cell.notes)
            }
        }
    }

    fun changeNoteTakingState() {
        isTakingNotes = !isTakingNotes
        isTakingNotesLiveData.postValue(isTakingNotes)

        val curNotes = if (isTakingNotes) {
            board.getCell(selectedRow, selectedCol).notes
        } else {
            setOf<Int>()
        }
        highlightedKeysLiveData.postValue(curNotes)
    }

    fun isGameSolved(): Boolean {
        return board.isSolved()
    }


}