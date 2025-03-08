/**
 * Created by [Youssef Hamdy] on 3/6/2025.
 */
package com.example.cybersapienttask.features.addtask.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.cybersapienttask.core.base.BaseViewModel
import com.example.cybersapienttask.data.source.local.db.entities.Task
import com.example.cybersapienttask.features.addtask.view.AddTaskIntent
import com.example.cybersapienttask.features.addtask.view.AddTaskSideEffect
import com.example.cybersapienttask.features.addtask.view.AddTaskState
import com.example.cybersapienttask.features.taskslist.view.TasksListIntent
import com.example.cybersapienttask.features.taskslist.view.TasksListSideEffect
import com.example.cybersapienttask.features.taskslist.view.TasksListState
import com.example.matbakhna.data.repos.TaskRepository
import kotlinx.coroutines.launch

class AddTaskViewModel(private val repository: TaskRepository)
    : BaseViewModel<AddTaskIntent, AddTaskState, AddTaskSideEffect>() {


    override fun initialState(): AddTaskState {

        return AddTaskState()
    }

    override fun handleEvents(event: AddTaskIntent) {
       when(event){
           is AddTaskIntent.AddTask -> {
               addTask(event.task)
               setEffect { AddTaskSideEffect.Navigation.BackToTaskList }
           }
       }
    }


    fun addTask(task: Task) = viewModelScope.launch {
        repository.addTask(task)
    }
}