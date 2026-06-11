package com.puce.habitatgest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.puce.habitatgest.presentation.navigation.AppNavGraph
import com.puce.habitatgest.presentation.theme.HabitatGestTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as HabitatApp).container

        setContent {
            HabitatGestTheme {
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController,
                    container     = container,
                )
            }
        }
    }
}
