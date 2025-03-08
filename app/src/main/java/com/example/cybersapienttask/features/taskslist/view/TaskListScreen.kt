/**
 * Created by [Youssef Hamdy] on 3/5/2025.
 */
package com.example.cybersapienttask.features.taskslist.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.cybersapienttask.data.source.local.db.entities.Priority
import com.example.cybersapienttask.data.source.local.db.entities.SortingType
import com.example.cybersapienttask.data.source.local.db.entities.Task
import com.example.cybersapienttask.data.source.local.db.entities.TaskCategory
import com.example.cybersapienttask.features.taskslist.viewmodel.TaskViewModel
import com.example.cybersapienttask.navigation.Screen
import com.example.cybersapienttask.ui.theme.CustomTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskListScreen(
    navController: NavHostController, viewModel: TaskViewModel
) {

    val sideEffect = viewModel.effect
    val tasks by viewModel.getFilteredTasks().collectAsState(initial = emptyList())
    val selectedTabIndex = remember { mutableStateOf(0) }

    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.8f else 1f,
        animationSpec = tween(durationMillis = 100, easing = LinearOutSlowInEasing)
    )

    val totalTasks = tasks.size
    val completedTasks =
        tasks.count { it.isCompleted } // Assuming isCompleted is a Boolean in Task model
    val progress = if (totalTasks > 0) completedTasks / totalTasks.toFloat() else 0f


    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )

    val snackbarHostState = remember { SnackbarHostState() }

    var expanded by remember { mutableStateOf(false) }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            TopAppBar(title = {
                Text(
                    stringResource(R.string.tasks_title),
                    modifier = Modifier.padding(10.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = typography.titleMedium.fontFamily
                )
            }, actions = {
                IconButton(onClick = {
                    viewModel.setEvent(TasksListIntent.openSettings)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Settings, contentDescription = "Settings"
                    )
                }
                Box {
                IconButton(onClick = {expanded = true}) {
                    Icon(
                        painter =painterResource(id =  R.drawable.sort_ic), contentDescription = "sorting"
                    )
                }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.sort_by_duedate)) },
                            onClick = {
                                expanded = false
                                viewModel.setEvent(TasksListIntent.SortedTasks(SortingType.DUEDATE))
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.sort_by_priority)) },
                            onClick = {
                                expanded = false
                                viewModel.setEvent(TasksListIntent.SortedTasks(SortingType.PRIORITY))
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.sort_by_alphabetical)) },
                            onClick = {
                                expanded = false
                                viewModel.setEvent(TasksListIntent.SortedTasks(SortingType.ALPHABETICAL))
                            }
                        )
                    }


                    }
            })

        },
        floatingActionButton = {
            Column {
                Box {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        strokeWidth = 8.dp,
                        modifier = Modifier.size(80.dp),

                        )
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%", // Convert progress to percentage
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(
                    onClick = {
                        isPressed = !isPressed
                        viewModel.setEvent(TasksListIntent.openAddTask)
                    }, modifier = Modifier.scale(scale)
                ) {
                    Icon(Icons.Filled.Add, "Floating action button.")
                }

            }
        }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = innerPadding.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(70.dp))
                    .border(
                        border = BorderStroke(
                            1.dp, CustomTheme.colors.gray
                        ), RoundedCornerShape(70.dp)
                    )
                    .height(IntrinsicSize.Min), contentAlignment = Alignment.Center
            ) {

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TaskCategory.entries.forEachIndexed { index, s ->
                        TabItem(
                            isSelected = index == selectedTabIndex.value,
                            text = s.name,
                            Modifier.weight(0.5f),
                            onClick = {
                                selectedTabIndex.value = index
                                viewModel.setEvent(TasksListIntent.ChangeCategory(s))

                            })
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (tasks.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn {
                    items(tasks, key = { it.id }) { task ->
                        SwipeToDeleteContainer(
                            task,
                            onDelete = { viewModel.setEvent(TasksListIntent.DeleteTask(task)) },
                            onRestore = { viewModel.setEvent(TasksListIntent.RestoreTask(task)) },
                            snackbarHostState = snackbarHostState
                        ) {
                            TaskItem(task = task, onTaskCompletedChange = {
                                viewModel.setEvent(TasksListIntent.EditTask(task))
                            }, onTaskClicked = {
                                viewModel.setEvent(
                                    TasksListIntent.openTaskDetails(task)
                                )
                            })
                        }


                    }
                }


            }
        }
    }


    LaunchedEffect("") {
        sideEffect.onEach { effect ->
            when (effect) {
                is TasksListSideEffect.Navigation.OpenAddTask -> {
                    navController.navigate(Screen.addTask.route)
                }

                is TasksListSideEffect.Navigation.OpenTaskDetails -> {
                    navController.navigate("details_screen/${effect.task.id}")
                }

                is TasksListSideEffect.Navigation.OpenSettings -> {
                    navController.navigate(Screen.settings.route)
                }
            }
        }.collect()
    }
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }

}

@Composable
fun TaskItem(task: Task, onTaskCompletedChange: (Task) -> Unit, onTaskClicked: (Task) -> Unit) {
    val elevation by animateDpAsState(
        targetValue = if (task.priority.toString() == "High") 8.dp else 4.dp
    )
    val isCompleted by rememberUpdatedState(newValue = task.isCompleted)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onTaskClicked(task) },
        elevation = CardDefaults.cardElevation(elevation)
    ) {

        Column(
            modifier = Modifier
                .background(
                    color = when (task.priority) {
                        Priority.HIGH -> CustomTheme.colors.highTask
                        Priority.MEDIUM -> CustomTheme.colors.mediumTask
                        Priority.LOW -> CustomTheme.colors.lowTask
                    }
                )
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()

            ) {
                TaskCompleteCircle(
                    modifier = Modifier,
                    isCompleted = isCompleted, onClick = {
                        task.isCompleted = it
                        onTaskCompletedChange(task)
                    },
                    size = 24
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = task.title, fontSize = 16.sp, modifier = Modifier.weight(1f)
                )
                Card(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(80.dp)
                        .clickable { /* Add navigation or action for task item click */ },
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = when (task.priority) {
                                    Priority.HIGH -> CustomTheme.colors.highTask
                                    Priority.MEDIUM -> CustomTheme.colors.mediumTask
                                    Priority.LOW -> CustomTheme.colors.lowTask
                                }
                            ),
                        contentAlignment = Alignment.Center


                    ) {

                        Text(
                            text = task.priority.name.lowercase(),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }


            }
            task.description?.let {
                Text(it, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint =MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = task.dueDate.toString(),
                    fontSize = 16.sp,
                    color =MaterialTheme.colorScheme.onSurface
                    ,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }


    }
}

@Composable
fun CompleteProgressIndicator(animatedProgress: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.padding(32.dp)) {
            CircularProgressIndicator(
                progress = { animatedProgress },
                strokeWidth = 8.dp,
                modifier = Modifier.size(80.dp),

                )
            Text(
                text = "${(animatedProgress * 100).toInt()}%", // Convert progress to percentage
                color = MaterialTheme.colorScheme.onSurface
                ,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Center),
            )
        }

    }
}

@Composable
fun TabItem(isSelected: Boolean, text: String, modifier: Modifier, onClick: () -> Unit) {
    val tabTextColor: Color by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onSecondary
        } else {
            MaterialTheme.colorScheme.onSurface
        }, animationSpec = tween(easing = LinearEasing), label = ""
    )

    val background: Color by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSecondary
        ,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        label = ""
    )

    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(1f)
            .background(background, RoundedCornerShape(64.dp))
            .clickable(
                interactionSource = interactionSource, indication = null
            ) {

                onClick.invoke()
            }
            .padding(vertical = 15.dp), contentAlignment = Alignment.Center) {
        Text(
            text = text.lowercase().replaceFirstChar { it.uppercaseChar() },
            style = typography.titleSmall,
            textAlign = TextAlign.Center,
            color = tabTextColor
        )
    }
}

@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    onRestore: (T) -> Unit,
    snackbarHostState: SnackbarHostState,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    val context = LocalContext.current

    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberSwipeToDismissBoxState(confirmValueChange = { value ->
        if (value == SwipeToDismissBoxValue.EndToStart || value == SwipeToDismissBoxValue.StartToEnd) {
            isRemoved = true
            true
        } else {
            isRemoved = false
            false
        }
    })

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            val result = snackbarHostState.showSnackbar(
                message = context.getString(R.string.task_deleted), actionLabel = "Undo", duration = SnackbarDuration.Short
            )

            if (result == SnackbarResult.ActionPerformed) {
                onRestore(item)
                isRemoved = false
            } else {
                delay(animationDuration.toLong())
                onDelete(item)
            }
        }
    }

    AnimatedVisibility(
        visible = !isRemoved, exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration), shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                DeleteBackground(swipeDismissState = state)
            },

            ) {
            content(item)

        }
    }
}

@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState
) {
    val color = if (swipeDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        Color.Red
    } else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondary

        )
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val preloaderLottieComposition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                R.raw.empty_tasks
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


        Text(
            stringResource(R.string.no_tasks_found),
            // modifier = Modifier.padding(top = 8.dp),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            fontWeight = FontWeight.Medium
        )
    }


}

