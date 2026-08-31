package com.ricewood.mathkids.model

/**
 * Singapore-style area/array model for multiplication: split the 2-digit
 * factor into tens + ones and multiply each part (distributive property),
 * e.g. 7 x 23 = (7 x 20) + (7 x 3).
 */
data class MultiplicationProblem(
    val a: Int,        // single-digit factor (multiplier), 2-9
    val b: Int,        // 1 or 2-digit factor
    val bTens: Int,    // tens part of b (0 if none)
    val bOnes: Int,    // ones part of b
    val partialTens: Int,  // a * bTens
    val partialOnes: Int,  // a * bOnes
    val product: Int
)

object MultiplicationGenerator {
    /**
     * level 1: times tables 2-5, single-digit x single-digit (array model, no split needed)
     * level 2: times tables 2-9, single-digit x single-digit
     * level 3: single-digit x 2-digit (11-20) using the area/split model
     */
    fun generate(level: Int): MultiplicationProblem {
        val a = when (level) {
            1 -> (2..5).random()
            2 -> (2..9).random()
            else -> (2..9).random()
        }
        val b = when (level) {
            1 -> (2..5).random()
            2 -> (2..9).random()
            else -> (11..20).random()
        }
        val bTens = (b / 10) * 10
        val bOnes = b % 10
        return MultiplicationProblem(
            a = a,
            b = b,
            bTens = bTens,
            bOnes = bOnes,
            partialTens = a * bTens,
            partialOnes = a * bOnes,
            product = a * b
        )
    }
}
