package com.vpdevs.localscholarai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vpdevs.localscholarai.ui.screens.HomeScreen
import com.vpdevs.localscholarai.ui.theme.LocalScholarAITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalScholarAITheme {
                HomeScreen()
            }
        }
    }
}
