/**
 * Created by [Youssef Hamdy] on 3/8/2025.
 */
package com.example.cybersapienttask.features.taskdetails.view

import com.example.cybersapienttask.core.base.ViewIntent
import com.example.cybersapienttask.data.source.local.db.entities.Task
import com.example.cybersapienttask.data.source.local.db.entities.TaskCategory

sealed class TaskDetailsIntent: ViewIntent {

    data class GetTaskDetails(val taskId:Int) : TaskDetailsIntent()
    data class EditTask(val task: Task) : TaskDetailsIntent()
    data class DeleteTask(val task: Task) : TaskDetailsIntent()

}
