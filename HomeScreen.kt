package com.ricewood.mathkids.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ricewood.mathkids.ui.components.StarBadge
import com.ricewood.mathkids.ui.theme.Blue
import com.ricewood.mathkids.ui.theme.Purple

@Composable
fun HomeScreen(
    stars: Int,
    onPickDivision: () -> Unit,
    onPickMultiplication: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🧮 MathKids", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Black)
        Text(
            "Learn multiplication & division the Singapore way",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
        StarBadge(stars = stars, modifier = Modifier.padding(bottom = 32.dp))

        OperationCard(
            title = "➗ Division",
            subtitle = "Step-by-step long division",
            color = Purple,
            onClick = onPickDivision
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(12.dp))
        OperationCard(
            title = "✖️ Multiplication",
            subtitle = "Arrays & the area model",
            color = Blue,
            onClick = onPickMultiplication
        )
    }
}

@Composable
private fun OperationCard(title: String, subtitle: String, color: androidx.compose.ui.graphics.Color, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f)
            .background(color.copy(alpha = 0.18f), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium, color = color)
        Text(subtitle, style = MaterialTheme.typography.bodyLarge)
    }
}
