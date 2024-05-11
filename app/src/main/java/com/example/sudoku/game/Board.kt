package com.example.sudoku.game

import com.example.sudoku.view.globalDifficulty


class Board(val size: Int, val cells: List<Cell>) {

    fun getCell(row: Int, col: Int) = cells[row * size + col]


    val easySolution = listOf(
        listOf(5, 3, 4, 6, 7, 8, 9, 1, 2),
        listOf(6, 7, 2, 1, 9, 5, 3, 4, 8),
        listOf(1, 9, 8, 3, 4, 2, 5, 6, 7),
        listOf(8, 5, 9, 7, 6, 1, 4, 2, 3),
        listOf(4, 2, 6, 8, 5, 3, 7, 9, 1),
        listOf(7, 1, 3, 9, 2, 4, 8, 5, 6),
        listOf(9, 6, 1, 5, 3, 7, 2, 8, 4),
        listOf(2, 8, 7, 4, 1, 9, 6, 3, 5),
        listOf(3, 4, 5, 2, 8, 6, 1, 7, 9)
    )


    val hardSolution = listOf(
        listOf(8, 1, 2, 7, 5, 3, 6, 4, 9),
        listOf(9, 4, 3, 6, 8, 2, 1, 7, 5),
        listOf(6, 7, 5, 4, 9, 1, 2, 8, 3),
        listOf(1, 5, 4, 2, 3, 7, 8, 9, 6),
        listOf(3, 6, 9, 8, 4, 5, 7, 2, 1),
        listOf(2, 8, 7, 1, 6, 9, 5, 3, 4),
        listOf(5, 2, 1, 9, 7, 4, 3, 6, 8),
        listOf(4, 3, 8, 5, 2, 6, 9, 1, 7),
        listOf(7, 9, 6, 3, 1, 8, 4, 5, 2)
    )

    // Check if the board matches the solution
    fun isSolved(): Boolean {
        val solution = when (globalDifficulty) {
            "easy" -> easySolution
            "hard" -> hardSolution
            else -> throw IllegalArgumentException("Invalid difficulty level")
        }

        // Check each cell against the solution
        for (row in 0 until size) {
            for (col in 0 until size) {
                val cellValue = getCell(row, col).value
                val solutionValue = solution[row][col]
                if (cellValue != solutionValue) {
                    return false
                }
            }
        }
        return true
    }
}
