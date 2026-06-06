package com.fizi.lifehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.fizi.lifehub.ui.navigation.LifeHubNavHost
import com.fizi.lifehub.ui.splash.SplashScreen
import com.fizi.lifehub.ui.theme.LifeHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LifeHubTheme {
                var showSplash by remember { mutableStateOf(true) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimatedContent(
                        targetState = showSplash,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(600)) togetherWith
                                    fadeOut(animationSpec = tween(400))
                        },
                        label = "splashTransition"
                    ) { isSplash ->
                        if (isSplash) {
                            SplashScreen(onSplashFinished = { showSplash = false })
                        } else {
                            LifeHubNavHost()
                        }
                    }
                }
            }
        }
    }
}
