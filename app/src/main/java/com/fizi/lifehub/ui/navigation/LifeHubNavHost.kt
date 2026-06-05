package com.fizi.lifehub.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fizi.lifehub.ui.budget.BudgetScreen
import com.fizi.lifehub.ui.habits.HabitScreen
import com.fizi.lifehub.ui.notes.NotesScreen
import com.fizi.lifehub.ui.theme.*
import com.fizi.lifehub.ui.todo.TodoScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector, val emoji: String) {
    data object Todo : Screen("todo", "Todo", Icons.Default.CheckCircle, "✅")
    data object Notes : Screen("notes", "Notes", Icons.Default.Edit, "📝")
    data object Budget : Screen("budget", "Budget", Icons.Default.Home, "💰")
    data object Habits : Screen("habits", "Habits", Icons.Default.Repeat, "🎯")
}

val bottomNavItems = listOf(
    Screen.Todo,
    Screen.Notes,
    Screen.Budget,
    Screen.Habits
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifeHubNavHost() {
    val navController = rememberNavController()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            // Floating Bottom Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(28.dp),
                            ambientColor = Color.Black.copy(alpha = 0.1f),
                            spotColor = Color.Black.copy(alpha = 0.1f)
                        ),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        bottomNavItems.forEach { screen ->
                            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        if (selected) Primary.copy(alpha = 0.1f)
                                        else Color.Transparent
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        screen.emoji,
                                        fontSize = if (selected) 22.sp else 18.sp
                                    )
                                    Text(
                                        screen.title,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selected) Primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (selected) {
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .background(Primary, CircleShape)
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }
                                }

                                // Clickable overlay
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clip(RoundedCornerShape(20.dp))
                                        .clickable {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Todo.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) +
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                        )
            },
            exitTransition = {
                fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMediumLow))
            }
        ) {
            composable(Screen.Todo.route) { TodoScreen() }
            composable(Screen.Notes.route) { NotesScreen() }
            composable(Screen.Budget.route) { BudgetScreen() }
            composable(Screen.Habits.route) { HabitScreen() }
        }
    }
}
