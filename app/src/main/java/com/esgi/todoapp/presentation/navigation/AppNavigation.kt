package com.esgi.todoapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.esgi.todoapp.presentation.screens.TaskDetailScreen
import com.esgi.todoapp.presentation.screens.TaskScreen

// Navigation routes
object NavRoutes {
    const val TASKS = "tasks"
    const val TASK_DETAIL = "task_detail/{taskId}"

    // Helper function to create a route with task ID
    fun taskDetail(taskId: Int) = "task_detail/$taskId"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavRoutes.TASKS
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = NavRoutes.TASKS) {
            TaskScreen(
                navigateToTaskDetail = { taskId ->
                    navController.navigate(NavRoutes.taskDetail(taskId))
                }
            )
        }

        // Écran de détail de tâche (à implémenter plus tard)
        composable(
            route = NavRoutes.TASK_DETAIL,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
            TaskDetailScreen(
                taskId = taskId,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}