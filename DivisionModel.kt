package com.ricewood.mathkids.model

/**
 * One "row" of a long-division working, mirroring the classic board layout:
 *   - bring down (or start with) a chunk of the dividend
 *   - ask "how many times does the divisor fit?" -> quotientDigit
 *   - multiply divisor * quotientDigit -> product
 *   - subtract -> remainder
 *   - (next step brings down the following digit, appended to remainder)
 */
data class DivisionStep(
    val stepIndex: Int,
    val chunk: Int,          // the number currently being divided at this step
    val quotientDigit: Int,  // correct digit for this step
    val product: Int,        // divisor * quotientDigit
    val remainder: Int,      // chunk - product
    val broughtDownDigit: Int?, // next dividend digit appended after this step, if any
    val endColumn: Int       // 0-based index (into the dividend's digits) of the last digit consumed by this step — used to align the on-screen worksheet
)

data class DivisionProblem(
    val dividend: Int,
    val divisor: Int,
    val steps: List<DivisionStep>,
    val quotient: Int,
    val remainder: Int
)

/**
 * Builds the full step-by-step breakdown of dividend ÷ divisor (divisor 1-12,
 * dividend up to 4 digits), the way it's taught on paper: digit by digit,
 * bring-down included. Leading zero quotient digits (when the first chunk
 * is smaller than the divisor) are represented as steps with quotientDigit = 0.
 */
fun buildLongDivisionSteps(dividend: Int, divisor: Int): DivisionProblem {
    val dividendDigits = dividend.toString().map { it - '0' }
    val steps = mutableListOf<DivisionStep>()

    var chunk = 0
    var digitPointer = 0
    var stepIndex = 0

    // Build up the first chunk until it's >= divisor (handles cases like 5)492 -> first chunk "4" < 5, so pull "49")
    chunk = dividendDigits[digitPointer]
    digitPointer++
    while (chunk < divisor && digitPointer < dividendDigits.size) {
        chunk = chunk * 10 + dividendDigits[digitPointer]
        digitPointer++
    }

    while (true) {
        val qDigit = chunk / divisor
        val product = qDigit * divisor
        val remainder = chunk - product
        val nextDigit = if (digitPointer < dividendDigits.size) dividendDigits[digitPointer] else null

        steps.add(
            DivisionStep(
                stepIndex = stepIndex,
                chunk = chunk,
                quotientDigit = qDigit,
                product = product,
                remainder = remainder,
                broughtDownDigit = nextDigit,
                endColumn = digitPointer - 1
            )
        )

        if (nextDigit == null) break
        chunk = remainder * 10 + nextDigit
        digitPointer++
        stepIndex++
    }

    val quotient = steps.joinToString("") { it.quotientDigit.toString() }.toInt()
    val remainder = steps.last().remainder

    return DivisionProblem(dividend, divisor, steps, quotient, remainder)
}

object DivisionGenerator {
    /**
     * level 1: divisor 2-5, dividend 2-digit, exact division (no remainder) — build confidence
     * level 2: divisor 2-6, dividend 2-3 digit, may have remainder
     * level 3: divisor 2-9, dividend 3-digit, may have remainder (like the board example: 492 ÷ 5)
     */
    fun generate(level: Int): DivisionProblem {
        val divisor = when (level) {
            1 -> (2..5).random()
            2 -> (2..6).random()
            else -> (2..9).random()
        }
        val dividend = when (level) {
            1 -> divisor * (2..9).random() // exact
            2 -> (10..99).random().coerceAtLeast(divisor + 1)
            else -> (100..999).random()
        }
        return buildLongDivisionSteps(dividend, divisor)
    }
}
