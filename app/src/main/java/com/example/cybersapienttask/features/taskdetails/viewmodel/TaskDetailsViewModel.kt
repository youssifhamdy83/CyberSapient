/**
 * Created by [Youssef Hamdy] on 3/8/2025.
 */
package com.example.cybersapienttask.features.taskdetails.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.cybersapienttask.core.base.BaseViewModel
import com.example.cybersapienttask.data.source.local.db.entities.Task
import com.example.cybersapienttask.features.taskdetails.view.TaskDetailsIntent
import com.example.cybersapienttask.features.taskdetails.view.TaskDetailsSideEffect
import com.example.cybersapienttask.features.taskdetails.view.TaskDetailsState
import com.example.matbakhna.data.repos.TaskRepository
import kotlinx.coroutines.launch

class TaskDetailsViewModel(private val repository: TaskRepository)
    : BaseViewModel<TaskDetailsIntent, TaskDetailsState, TaskDetailsSideEffect>() {


    override fun initialState(): TaskDetailsState {
        return TaskDetailsState()
    }

    override fun handleEvents(event: TaskDetailsIntent) {
        when (event) {
            is TaskDetailsIntent.GetTaskDetails -> {
                getTaskDetails(event.taskId)
            }
            is TaskDetailsIntent.DeleteTask->{
                deleteTask(event.task)
                setEffect{TaskDetailsSideEffect.Navigation.BackToTaskList}

            }
            is TaskDetailsIntent.EditTask -> {

            }

        }
    }


      fun getTaskDetails(taskId: Int) =viewModelScope.launch {
         val task =  repository.getTaskTaskDetails(taskId)
         // _state.value = TaskDetailsState(taskDetails = task)
           setState { copy(taskDetails = task,isLoading = false) }
       }





    fun updateTask(task: Task) = viewModelScope.launch {
        repository.updateTask(task)

    }
    fun deleteTask(task: Task) = viewModelScope.launch { repository.deleteTask(task) }
}