/**
 * Created by [Youssef Hamdy] on 3/5/2025.
 */
package com.example.cybersapienttask.features.taskslist.view

import com.example.cybersapienttask.core.base.ViewState
import com.example.cybersapienttask.data.source.local.db.entities.Task
import com.example.cybersapienttask.data.source.local.db.entities.TaskCategory
import kotlinx.coroutines.flow.Flow

data class TasksListState(
    val allTasks: Flow<List<Task>>?=null,
    val selectedCategory: TaskCategory = TaskCategory.ALL,
    val isLoading: Boolean?=true,
    ) : ViewState