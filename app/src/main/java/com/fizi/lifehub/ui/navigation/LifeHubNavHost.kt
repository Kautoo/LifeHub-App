package com.fizi.lifehub.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.fizi.lifehub.ui.calendar.CalendarScreen
import com.fizi.lifehub.ui.goals.GoalScreen
import com.fizi.lifehub.ui.habits.HabitScreen
import com.fizi.lifehub.ui.home.HomeScreen
import com.fizi.lifehub.ui.journal.JournalScreen
import com.fizi.lifehub.ui.more.MoreScreen
import com.fizi.lifehub.ui.notes.NotesScreen
import com.fizi.lifehub.ui.pomodoro.PomodoroScreen
import com.fizi.lifehub.ui.settings.SettingsScreen
import com.fizi.lifehub.ui.theme.*
import com.fizi.lifehub.ui.todo.TodoScreen
import com.fizi.lifehub.ui.water.WaterScreen

sealed class Screen(val route: String, val title: String, val emoji: String) {
    data object Home : Screen("home", "Home", "🏠")
    data object Todo : Screen("todo", "Todo", "✅")
    data object Notes : Screen("notes", "Notes", "📝")
    data object Habits : Screen("habits", "Habits", "🎯")
    data object More : Screen("more", "More", "☰")
    // Secondary screens (not in bottom nav)
    data object Budget : Screen("budget", "Budget", "💰")
    data object Calendar : Screen("calendar", "Calendar", "📅")
    data object Journal : Screen("journal", "Journal", "📓")
    data object Goals : Screen("goals", "Goals", "🎯")
    data object Water : Screen("water", "Water", "💧")
    data object Pomodoro : Screen("pomodoro", "Pomodoro", "🍅")
    data object Settings : Screen("settings", "Settings", "⚙️")
}

val bottomNavItems = listOf(
    Screen.Home, Screen.Todo, Screen.Notes, Screen.Habits, Screen.More
)

@Composable
fun LifeHubNavHost() {
    val navController = rememberNavController()
    var isDarkMode by remember { mutableStateOf(false) }

    LifeHubTheme(darkTheme = isDarkMode) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 24.dp,
                                shape = RoundedCornerShape(28.dp),
                                ambientColor = Color.Black.copy(alpha = 0.08f),
                                spotColor = Color.Black.copy(alpha = 0.08f)
                            ),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination

                            bottomNavItems.forEach { screen ->
                                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(
                                            if (selected) Primary.copy(alpha = 0.1f)
                                            else Color.Transparent
                                        )
                                        .clickable {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Text(screen.emoji, fontSize = if (selected) 22.sp else 18.sp)
                                        Text(
                                            screen.title,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selected) Primary
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        if (selected) {
                                            Box(modifier = Modifier.size(4.dp).background(Primary, CircleShape))
                                        } else {
                                            Spacer(modifier = Modifier.height(4.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
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
                composable(Screen.Home.route) {
                    HomeScreen(
                        onNavigateToTodo = { navController.navigate(Screen.Todo.route) },
                        onNavigateToNotes = { navController.navigate(Screen.Notes.route) },
                        onNavigateToBudget = { navController.navigate(Screen.Budget.route) },
                        onNavigateToHabits = { navController.navigate(Screen.Habits.route) }
                    )
                }
                composable(Screen.Todo.route) { TodoScreen() }
                composable(Screen.Notes.route) { NotesScreen() }
                composable(Screen.Habits.route) { HabitScreen() }
                composable(Screen.More.route) {
                    MoreScreen(
                        onNavigateToBudget = { navController.navigate(Screen.Budget.route) },
                        onNavigateToCalendar = { navController.navigate(Screen.Calendar.route) },
                        onNavigateToJournal = { navController.navigate(Screen.Journal.route) },
                        onNavigateToGoals = { navController.navigate(Screen.Goals.route) },
                        onNavigateToWater = { navController.navigate(Screen.Water.route) },
                        onNavigateToPomodoro = { navController.navigate(Screen.Pomodoro.route) },
                        onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                    )
                }
                composable(Screen.Budget.route) { BudgetScreen() }
                composable(Screen.Calendar.route) { CalendarScreen() }
                composable(Screen.Journal.route) { JournalScreen() }
                composable(Screen.Goals.route) { GoalScreen() }
                composable(Screen.Water.route) { WaterScreen() }
                composable(Screen.Pomodoro.route) { PomodoroScreen() }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { isDarkMode = !isDarkMode }
                    )
                }
            }
        }
    }
}
