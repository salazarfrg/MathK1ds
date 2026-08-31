package com.ricewood.mathkids.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ricewood.mathkids.ui.theme.Gold
import com.ricewood.mathkids.ui.theme.Green
import com.ricewood.mathkids.ui.theme.Red

@Composable
fun StarBadge(stars: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color(0x33F5B301), RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(Icons.Filled.Star, contentDescription = null, tint = Gold)
        Text("$stars", fontWeight = FontWeight.Bold, color = Gold)
    }
}

/** A row of tappable number choices, one of which is correct. */
@Composable
fun ChoicePad(
    options: List<Int>,
    disabled: Boolean,
    onChoice: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(top = 8.dp)
    ) {
        options.forEach { option ->
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .clickable(enabled = !disabled) { onChoice(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/** Friendly, non-punishing feedback: gentle on mistakes, celebratory on success. */
@Composable
fun FeedbackBubble(result: FeedbackResult?) {
    AnimatedVisibility(
        visible = result != null,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut()
    ) {
        val (msg, color) = when (result) {
            FeedbackResult.CORRECT -> "¡Muy bien! 🎉" to Green
            FeedbackResult.WRONG -> "Casi… ¡inténtalo de nuevo! 💪" to Red
            null -> "" to Color.Transparent
        }
        Box(
            modifier = Modifier
                .background(color.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
                .padding(PaddingValues(horizontal = 16.dp, vertical = 10.dp))
        ) {
            Text(msg, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

enum class FeedbackResult { CORRECT, WRONG }

@Composable
fun bounce(target: Float): Float {
    val v by animateFloatAsState(
        targetValue = target,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "bounce"
    )
    return v
}
