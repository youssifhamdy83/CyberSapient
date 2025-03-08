/**
 * Created by [Youssef Hamdy] on 3/8/2025.
 */
package com.example.cybersapienttask.features.taskdetails.view

import com.example.cybersapienttask.core.base.ViewState
import com.example.cybersapienttask.data.source.local.db.entities.Task
import com.example.cybersapienttask.data.source.local.db.entities.TaskCategory
import kotlinx.coroutines.flow.Flow

data class TaskDetailsState(
    val taskDetails: Flow<Task>?=null,
    val isLoading: Boolean=true,
    ) : ViewState