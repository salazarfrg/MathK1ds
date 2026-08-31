package com.ricewood.mathkids.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ricewood.mathkids.model.DivisionGenerator
import com.ricewood.mathkids.model.DivisionProblem
import com.ricewood.mathkids.model.MultiplicationGenerator
import com.ricewood.mathkids.model.MultiplicationProblem

/**
 * Holds cross-screen game state: stars earned, current streak, and the
 * adaptive level per operation (goes up after 3 correct in a row, eases
 * back down after 2 misses — keeps kids in their challenge zone).
 */
class GameViewModel : ViewModel() {

    var stars by mutableIntStateOf(0)
        private set
    var streak by mutableIntStateOf(0)
        private set

    var divisionLevel by mutableIntStateOf(1)
        private set
    var multiplicationLevel by mutableIntStateOf(1)
        private set

    var currentDivision by mutableStateOf<DivisionProblem?>(null)
        private set
    var currentMultiplication by mutableStateOf<MultiplicationProblem?>(null)
        private set

    fun newDivisionProblem() {
        currentDivision = DivisionGenerator.generate(divisionLevel)
    }

    fun newMultiplicationProblem() {
        currentMultiplication = MultiplicationGenerator.generate(multiplicationLevel)
    }

    fun onCorrect(operation: Operation) {
        stars++
        streak++
        if (streak >= 3) {
            streak = 0
            when (operation) {
                Operation.DIVISION -> divisionLevel = (divisionLevel + 1).coerceAtMost(3)
                Operation.MULTIPLICATION -> multiplicationLevel = (multiplicationLevel + 1).coerceAtMost(3)
            }
        }
    }

    fun onMistake(operation: Operation) {
        streak = 0
        when (operation) {
            Operation.DIVISION -> divisionLevel = (divisionLevel - 1).coerceAtLeast(1)
            Operation.MULTIPLICATION -> multiplicationLevel = (multiplicationLevel - 1).coerceAtLeast(1)
        }
    }
}

enum class Operation { DIVISION, MULTIPLICATION }
