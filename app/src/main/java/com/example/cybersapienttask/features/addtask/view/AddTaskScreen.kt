/**
 * Created by [Youssef Hamdy] on 3/6/2025.
 */
package com.example.cybersapienttask.features.addtask.view
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cybersapienttask.R
import com.example.cybersapienttask.data.source.local.db.entities.Priority
import com.example.cybersapienttask.data.source.local.db.entities.Task
import com.example.cybersapienttask.features.addtask.viewmodel.AddTaskViewModel
import com.example.cybersapienttask.ui.theme.CustomTheme
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavHostController) {
    val viewModel: AddTaskViewModel = koinViewModel()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var selectedDate by remember { mutableStateOf(getTodayDate()) }
    var openDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val onDateSelected: (Int, Int, Int) -> Unit = { year, month, dayOfMonth ->
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        selectedDate =
            "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${
                calendar.get(Calendar.YEAR)
            }"
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

        ) {

            TopAppBar(
                title = { Text(stringResource(R.string.add_task)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )

            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.title)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.description)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Text(
                stringResource(R.string.select_date),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { openDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Date",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    selectedDate,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (openDialog) {
                DatePickerDialogView(
                    onDateSelected = onDateSelected, onDismissRequest = { openDialog = false })
            }


            Text(
                stringResource(R.string.select_priority),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround // Distribute items evenly
            ) {
                Priority.values().forEach { priority ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { selectedPriority = priority }
                            .padding(8.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(50.dp)
                                .background(
                                    color = when (priority) {
                                        Priority.HIGH -> CustomTheme.colors.highTask
                                        Priority.MEDIUM -> CustomTheme.colors.mediumTask
                                        Priority.LOW -> CustomTheme.colors.lowTask
                                    },

                                    )
                                .border(
                                    width = 2.dp,
                                    color = if (selectedPriority == priority) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                    shape = CircleShape
                                )

                        )

                        Text(
                            text = when (priority) {
                                Priority.HIGH -> stringResource(R.string.high)
                                Priority.MEDIUM -> stringResource(R.string.medium)
                                Priority.LOW -> stringResource(R.string.low)

                            }, style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }



            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    if (title.isEmpty()) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.empty_title),
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        viewModel.addTask(
                            Task(
                                title = title,
                                description = description,
                                priority = selectedPriority,
                                priorityLevel = selectedPriority.value,
                                dueDate = selectedDate
                            )
                        )
                        navController.popBackStack()
                    }

                })
            {
                Text(
                    stringResource(R.string.save_task),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}


@Composable
fun DatePickerDialogView(onDateSelected: (Int, Int, Int) -> Unit, onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(year, month, dayOfMonth)
            onDismissRequest()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    LaunchedEffect(Unit) {
        datePickerDialog.show()
    }
}

fun getTodayDate(): String {
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1
    val year = calendar.get(Calendar.YEAR)
    return "$day/$month/$year"
}
