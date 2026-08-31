package com.ricewood.mathkids

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ricewood.mathkids.ui.screens.DivisionScreen
import com.ricewood.mathkids.ui.screens.HomeScreen
import com.ricewood.mathkids.ui.screens.MultiplicationScreen
import com.ricewood.mathkids.ui.theme.MathKidsTheme
import com.ricewood.mathkids.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MathKidsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MathKidsApp()
                }
            }
        }
    }
}

@Composable
fun MathKidsApp() {
    val navController: NavHostController = rememberNavController()
    val gameViewModel: GameViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                stars = gameViewModel.stars,
                onPickDivision = { navController.navigate("division") },
                onPickMultiplication = { navController.navigate("multiplication") }
            )
        }
        composable("division") {
            DivisionScreen(viewModel = gameViewModel, onBack = { navController.popBackStack() })
        }
        composable("multiplication") {
            MultiplicationScreen(viewModel = gameViewModel, onBack = { navController.popBackStack() })
        }
    }
}
