package com.ricewood.mathkids.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ricewood.mathkids.model.MultiplicationProblem
import com.ricewood.mathkids.ui.components.ChoicePad
import com.ricewood.mathkids.ui.components.FeedbackBubble
import com.ricewood.mathkids.ui.components.FeedbackResult
import com.ricewood.mathkids.ui.components.StarBadge
import com.ricewood.mathkids.ui.theme.Blue
import com.ricewood.mathkids.ui.theme.Gold
import com.ricewood.mathkids.ui.theme.Green
import com.ricewood.mathkids.viewmodel.GameViewModel
import com.ricewood.mathkids.viewmodel.Operation

@Composable
fun MultiplicationScreen(viewModel: GameViewModel, onBack: () -> Unit) {
    LaunchedEffect(Unit) {
        if (viewModel.currentMultiplication == null) viewModel.newMultiplicationProblem()
    }
    val problem = viewModel.currentMultiplication ?: return

    var solved by remember(problem) { mutableStateOf(false) }
    var feedback by remember(problem) { mutableStateOf<FeedbackResult?>(null) }
    var choiceDisabled by remember(problem) { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") }
            Text("Level ${viewModel.multiplicationLevel}", style = MaterialTheme.typography.labelLarge)
            StarBadge(stars = viewModel.stars)
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "${problem.a} × ${problem.b}",
            style = MaterialTheme.typography.displayLarge,
            color = Blue
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (problem.bTens > 0) {
            // Area/split model: a x b = a x tens + a x ones
            Text(
                "Split ${problem.b} into ${problem.bTens} + ${problem.bOnes}:",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AreaBlock(
                    label = "${problem.a} × ${problem.bTens}",
                    value = problem.partialTens,
                    color = Gold,
                    weight = problem.bTens.toFloat()
                )
                AreaBlock(
                    label = "${problem.a} × ${problem.bOnes}",
                    value = problem.partialOnes,
                    color = Green,
                    weight = problem.bOnes.toFloat().coerceAtLeast(1f)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "${problem.partialTens} + ${problem.partialOnes} = ?",
                style = MaterialTheme.typography.headlineMedium
            )
        } else {
            // Simple array model for single-digit x single-digit facts
            ArrayGrid(rows = problem.a, cols = problem.b)
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!solved) {
            val options = remember(problem) { buildMultOptions(problem.product) }
            ChoicePad(options = options, disabled = choiceDisabled) { picked ->
                if (picked == problem.product) {
                    feedback = FeedbackResult.CORRECT
                    choiceDisabled = true
                    solved = true
                    viewModel.onCorrect(Operation.MULTIPLICATION)
                } else {
                    feedback = FeedbackResult.WRONG
                    viewModel.onMistake(Operation.MULTIPLICATION)
                }
            }
        } else {
            Text(
                "🎉 ${problem.a} × ${problem.b} = ${problem.product}",
                style = MaterialTheme.typography.headlineMedium, color = Green
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.newMultiplicationProblem() }) {
                Text("Next problem")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        FeedbackBubble(result = feedback)
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.AreaBlock(
    label: String, value: Int, color: androidx.compose.ui.graphics.Color, weight: Float
) {
    Column(
        modifier = Modifier
            .weight(weight)
            .background(color.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = color, style = MaterialTheme.typography.bodyLarge)
        Text(value.toString(), color = color, style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
private fun ArrayGrid(rows: Int, cols: Int) {
    Text("Count the dots: $rows rows of $cols", style = MaterialTheme.typography.bodyLarge)
    Spacer(modifier = Modifier.height(8.dp))
    LazyVerticalGrid(
        columns = GridCells.Fixed(cols),
        modifier = Modifier.height((rows * 34).dp).fillMaxWidth()
    ) {
        items(rows * cols) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .padding(3.dp)
                    .size(24.dp)
                    .background(Blue, RoundedCornerShape(50))
            )
        }
    }
}

private fun buildMultOptions(correct: Int): List<Int> {
    val opts = mutableSetOf(correct)
    while (opts.size < 4) {
        val delta = listOf(-10, -5, -2, -1, 1, 2, 5, 10).random()
        val candidate = (correct + delta).coerceAtLeast(0)
        opts.add(candidate)
    }
    return opts.toList().shuffled()
}
