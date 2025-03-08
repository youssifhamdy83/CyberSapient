/**
 * Created by [Youssef Hamdy] on 3/5/2025.
 */
package com.example.cybersapienttask.features.taskslist.view

import com.example.cybersapienttask.core.base.ViewIntent
import com.example.cybersapienttask.data.source.local.db.entities.SortingType
import com.example.cybersapienttask.data.source.local.db.entities.Task
import com.example.cybersapienttask.data.source.local.db.entities.TaskCategory

sealed class TasksListIntent: ViewIntent {
    object openAddTask : TasksListIntent()
    object GetAllTask : TasksListIntent()
    object openSettings : TasksListIntent()
    data class ChangeCategory(val category: TaskCategory) : TasksListIntent()
    data class EditTask(val task: Task) : TasksListIntent()
    data class openTaskDetails(val task: Task) : TasksListIntent()
    data class DeleteTask(val task: Task) : TasksListIntent()
    data class RestoreTask(val task: Task) : TasksListIntent()
    data class SortedTasks(val sortingType: SortingType) : TasksListIntent()



}
