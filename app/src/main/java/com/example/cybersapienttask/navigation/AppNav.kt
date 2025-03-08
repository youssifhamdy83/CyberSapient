/**
 * Created by [Youssef Hamdy] on 3/5/2025.
 */
package com.example.cybersapienttask.navigation
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cybersapienttask.features.addtask.view.AddTaskScreen
import com.example.cybersapienttask.features.taskslist.view.TaskListScreen
import com.example.cybersapienttask.features.taskslist.viewmodel.TaskViewModel
import com.example.cybersapienttask.features.settings.view.SettingsScreen
import com.example.cybersapienttask.features.taskdetails.view.TaskDetailsScreen
import com.example.cybersapienttask.features.taskdetails.viewmodel.TaskDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun AppNav(
    modifier: Modifier, navController: NavHostController,
    viewModel: TaskViewModel = koinViewModel()
) {

    NavHost(
        navController = navController,
        startDestination = Screen.taskList.route,
    ) {


        composable(route = Screen.taskList.route) { TaskListScreen(navController, viewModel) }
        composable(route = Screen.addTask.route) {
            AddTaskScreen(navController) }
        composable(route = Screen.settings.route) { SettingsScreen(navController) }

        composable(
            route = Screen.Details.route
        ) { backStackEntry ->
                 val viewModel: TaskDetailsViewModel = koinViewModel()
            val taskId = backStackEntry.arguments?.getString("taskId")?.toInt() // Get taskId from the route
            taskId?.let {
                TaskDetailsScreen(
                    navController=navController,
                    taskId = it,
                    viewModel = viewModel
                )
            }
        }
    }



}


sealed class Screen(val route: String) {
    object settings : Screen(route = "settings_screen")
    object taskList : Screen(route = "taskList_screen")
    object Details : Screen(route = "details_screen/{taskId}")
    object addTask : Screen(route = "add_task_screen")
    object Splash : Screen(route = "splash_screen")

}


