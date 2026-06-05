package com.fizi.lifehub.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Todo : Screen("todo", "Todo", Icons.Default.CheckCircle)
    data object Notes : Screen("notes", "Notes", Icons.Default.Edit)
    data object Budget : Screen("budget", "Budget", Icons.Default.Home)
    data object Habits : Screen("habits", "Habits", Icons.Default.Repeat)
}

val bottomNavItems = listOf(
    Screen.Todo,
    Screen.Notes,
    Screen.Budget,
    Screen.Habits
)
