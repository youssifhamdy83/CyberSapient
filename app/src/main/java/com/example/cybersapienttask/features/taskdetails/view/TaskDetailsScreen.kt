/**
 * Created by [Youssef Hamdy] on 3/8/2025.
 */
package com.example.cybersapienttask.features.taskdetails.view

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cybersapienttask.R
import com.example.cybersapienttask.core.utils.TaskCompleteCircle
import com.example.cybersapienttask.data.source.local.db.entities.Task
import com.example.cybersapienttask.features.taskdetails.viewmodel.TaskDetailsViewModel
import com.example.cybersapienttask.ui.theme.CustomTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: TaskDetailsViewModel,
    taskId: Int

) {
    var isVisible by remember { mutableStateOf(false) }
    val sideEffect = viewModel.effect

    val state = viewModel.viewState.value
    val task = state.taskDetails?.collectAsState(initial = null)?.value
    val radius by animateFloatAsState(
        targetValue = if (isVisible) 2000f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "radiusAnimation",
    )

    LaunchedEffect(Unit) {

    }
    LaunchedEffect(Unit) {
        isVisible = true
        viewModel.setEvent(TaskDetailsIntent.GetTaskDetails(taskId))
        sideEffect.onEach { effect ->
            when (effect) {
                is TaskDetailsSideEffect.Navigation.BackToTaskList -> {
                    navController.popBackStack()

                }

            }
        }.collect()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = radius / 2000f
                scaleY = radius / 2000f
                clip = true
            }
            .background(color = MaterialTheme.colorScheme.surface)
            .then(modifier),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text(stringResource(R.string.task_details)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },

                )

            when {
                state.isLoading -> CircularProgressIndicator()
                else -> {
                    if (task != null) {
                        TaskDetails(task = task,
                            onTaskCompletedChange = viewModel::updateTask,
                            onTaskDeleted = {
                                viewModel.setEvent(TaskDetailsIntent.DeleteTask(it))

                            })
                    }

                }
            }
            AnimationView()

        }
    }


}

@Composable
fun TaskDetails(task: Task, onTaskCompletedChange: (Task) -> Unit, onTaskDeleted: (Task) -> Unit) {
    val isCompleted by rememberUpdatedState(newValue = task.isCompleted)
    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Ensure card background is transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CustomTheme.colors.completedProgress,
                            CustomTheme.colors.remainingProgress
                        )
                    ), shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp) // Apply padding inside
        ) {
            Column(

                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.title),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.surface
                    )
                    Text(
                        text = task.title,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp), // Adds spacing above and below the line
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.5f) // Semi-transparent white
                    )
                }

                task.description?.let {
                    Column {
                        Text(
                            "Description ",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.surface
                        )
                        Text(
                            it,
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(8.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp), // Adds spacing above and below the line
                            thickness = 1.dp,
                            color = Color.White.copy(alpha = 0.5f) // Semi-transparent white
                        )
                    }
                }
                Column {
                    Text("Priority ", fontSize = 16.sp, color = MaterialTheme.colorScheme.surface)
                    Text(
                        text = task.priority.name.lowercase(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp), // Adds spacing above and below the line
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.5f) // Semi-transparent white
                    )
                }
                Column {
                    Text("Data ", fontSize = 16.sp, color = MaterialTheme.colorScheme.surface)
                    Text(
                        text = task.dueDate.toString(), color = MaterialTheme.colorScheme.onSurface
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp), // Adds spacing above and below the line
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.5f) // Semi-transparent white
                    )
                }


                Spacer(modifier = Modifier.size(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(color = Color.Red)
                            .padding(12.dp)
                            .clickable {
                                showDialog = true
                            }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(R.string.deleted_task),
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.size(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(color = Color.Blue)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TaskCompleteCircle(
                            modifier = Modifier, isCompleted = isCompleted, onClick = {
                                val updatedTask = task.copy(isCompleted = it)
                                onTaskCompletedChange(updatedTask)
                            }, size = 24
                        )
                        Text(
                            text = stringResource(R.string.completed_task),
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimationView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {

        val preloaderLottieComposition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                R.raw.details_animation
            )
        )

        val preloaderProgress by animateLottieCompositionAsState(
            preloaderLottieComposition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true
        )
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
        ) {
            LottieAnimation(
                composition = preloaderLottieComposition,
                progress = preloaderProgress,
                modifier = Modifier
                    .clipToBounds()
                    .scale(1.2f) // Removes extra space

            )
        }
    }
}



