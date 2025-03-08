/**
 * Created by [Youssef Hamdy] on 3/5/2025.
 */
package com.example.cybersapienttask.features.addtask.view

import com.example.cybersapienttask.core.base.ViewIntent
import com.example.cybersapienttask.data.source.local.db.entities.Task
import com.example.cybersapienttask.data.source.local.db.entities.TaskCategory

sealed class AddTaskIntent: ViewIntent {
    data class AddTask(val task: Task) : AddTaskIntent()
}
