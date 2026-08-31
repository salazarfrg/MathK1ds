package com.ricewood.mathkids.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ricewood.mathkids.model.DivisionProblem
import com.ricewood.mathkids.model.DivisionStep
import com.ricewood.mathkids.ui.components.ChoicePad
import com.ricewood.mathkids.ui.components.FeedbackBubble
import com.ricewood.mathkids.ui.components.FeedbackResult
import com.ricewood.mathkids.ui.components.StarBadge
import com.ricewood.mathkids.ui.theme.Gold
import com.ricewood.mathkids.ui.theme.Purple
import com.ricewood.mathkids.ui.theme.Red
import com.ricewood.mathkids.ui.theme.TextLight
import com.ricewood.mathkids.viewmodel.GameViewModel
import com.ricewood.mathkids.viewmodel.Operation

private enum class Phase { ASK, REVEAL, FINISHED }

@Composable
fun DivisionScreen(viewModel: GameViewModel, onBack: () -> Unit) {
    LaunchedEffect(Unit) {
        if (viewModel.currentDivision == null) viewModel.newDivisionProblem()
    }
    val problem = viewModel.currentDivision ?: return

    var completedSteps by remember(problem) { mutableIntStateOf(0) }
    var phase by remember(problem) { mutableStateOf(Phase.ASK) }
    var feedback by remember(problem) { mutableStateOf<FeedbackResult?>(null) }
    var choiceDisabled by remember(problem) { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") }
            Text("Level ${viewModel.divisionLevel}", style = MaterialTheme.typography.labelLarge)
            StarBadge(stars = viewModel.stars)
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "How many times does ${problem.divisor} fit? Build the answer digit by digit, just like on paper.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(20.dp))

        LongDivisionWorksheet(problem = problem, completedSteps = completedSteps)

        Spacer(modifier = Modifier.height(28.dp))

        when (phase) {
            Phase.ASK -> {
                val step = problem.steps[completedSteps]
                Text(
                    "${step.chunk} ÷ ${problem.divisor} = ?",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Purple
                )
                val options = remember(step) { buildOptions(step.quotientDigit) }
                ChoicePad(options = options, disabled = choiceDisabled) { picked ->
                    if (picked == step.quotientDigit) {
                        feedback = FeedbackResult.CORRECT
                        choiceDisabled = true
                        phase = Phase.REVEAL
                    } else {
                        feedback = FeedbackResult.WRONG
                        viewModel.onMistake(Operation.DIVISION)
                    }
                }
            }
            Phase.REVEAL -> {
                val step = problem.steps[completedSteps]
                Column {
                    Text(
                        "${problem.divisor} × ${step.quotientDigit} = ${step.product}",
                        color = Gold, style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        "${step.chunk} − ${step.product} = ${step.remainder}",
                        color = TextLight, style = MaterialTheme.typography.headlineMedium
                    )
                    if (step.broughtDownDigit != null) {
                        Text(
                            "↓ Bring down the ${step.broughtDownDigit}: makes ${step.remainder}${step.broughtDownDigit}",
                            color = Red, style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = {
                        viewModel.onCorrect(Operation.DIVISION)
                        feedback = null
                        choiceDisabled = false
                        completedSteps += 1
                        phase = if (completedSteps >= problem.steps.size) Phase.FINISHED else Phase.ASK
                    }) {
                        Text(if (completedSteps + 1 >= problem.steps.size) "Finish" else "Continue")
                    }
                }
            }
            Phase.FINISHED -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("🎉 ${problem.dividend} ÷ ${problem.divisor} = ${problem.quotient}" +
                            if (problem.remainder > 0) " remainder ${problem.remainder}" else "",
                        style = MaterialTheme.typography.headlineMedium, color = Purple)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.newDivisionProblem() }) {
                        Text("Next problem")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        FeedbackBubble(result = feedback)
    }
}

private fun buildOptions(correct: Int): List<Int> {
    val opts = mutableSetOf(correct)
    while (opts.size < 4) {
        val delta = (-2..2).random()
        val candidate = (correct + delta).coerceIn(0, 9)
        opts.add(candidate)
    }
    return opts.toList().shuffled()
}

/** Renders the classic pencil-and-paper layout using a monospace grid so columns line up automatically. */
@Composable
private fun LongDivisionWorksheet(problem: DivisionProblem, completedSteps: Int) {
    val dividendStr = problem.dividend.toString()
    val width = dividendStr.length

    val quotientRow = buildString {
        repeat(width) { append(' ') }
    }.toCharArray()
    for (i in 0 until completedSteps) {
        val s = problem.steps[i]
        quotientRow[s.endColumn] = ('0' + s.quotientDigit)
    }
    // also show the digit currently being worked on once revealed via REVEAL phase is handled by caller re-invoking with completedSteps

    val mono = FontFamily.Monospace

    Row(verticalAlignment = Alignment.Top) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(" ", fontFamily = mono, fontSize = 30.sp) // spacer to align with quotient row
            Text(problem.divisor.toString(), fontFamily = mono, fontSize = 30.sp, color = Gold, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(String(quotientRow), fontFamily = mono, fontSize = 30.sp, color = Purple, fontWeight = FontWeight.Bold)
            RedLine(width)
            Row {
                Text("⌐", fontFamily = mono, fontSize = 30.sp, color = Red)
                Text(dividendStr, fontFamily = mono, fontSize = 30.sp, color = TextLight)
            }
            for (i in 0 until completedSteps) {
                val s = problem.steps[i]
                val productStr = rowString(s.product, s.endColumn, width)
                Text("−$productStr", fontFamily = mono, fontSize = 24.sp, color = Gold)
                RedLine(width)
                val remainderStr = rowString(s.remainder, s.endColumn, width)
                Text(remainderStr, fontFamily = mono, fontSize = 24.sp, color = TextLight)
            }
        }
    }
}

@Composable
private fun RedLine(width: Int) {
    Text("─".repeat(width + 2), color = Red, fontFamily = FontFamily.Monospace)
}

private fun rowString(number: Int, endColumn: Int, totalWidth: Int): String {
    val numStr = number.toString()
    val leftPad = (endColumn + 1 - numStr.length).coerceAtLeast(0)
    val core = " ".repeat(leftPad) + numStr
    val rightPad = (totalWidth - core.length).coerceAtLeast(0)
    return core + " ".repeat(rightPad)
}
