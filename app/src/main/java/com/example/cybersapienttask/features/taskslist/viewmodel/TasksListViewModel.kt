/**
 * Created by [Youssef Hamdy] on 3/5/2025.
 */
package com.example.cybersapienttask.features.taskslist.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.cybersapienttask.core.base.BaseViewModel
import com.example.cybersapienttask.data.source.local.db.entities.SortingType
import com.example.cybersapienttask.data.source.local.db.entities.Task
import com.example.cybersapienttask.data.source.local.db.entities.TaskCategory
import com.example.cybersapienttask.features.taskslist.view.TasksListIntent
import com.example.cybersapienttask.features.taskslist.view.TasksListSideEffect
import com.example.cybersapienttask.features.taskslist.view.TasksListState
import com.example.matbakhna.data.repos.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) :
    BaseViewModel<TasksListIntent, TasksListState, TasksListSideEffect>() {



    init {
        setEvent(TasksListIntent.GetAllTask)

    }

    override fun initialState(): TasksListState {
        return TasksListState()
    }

    override fun handleEvents(event: TasksListIntent) {
        when (event) {
            is TasksListIntent.GetAllTask -> {
                getAllTasks()
            }

            is TasksListIntent.openAddTask -> {
                setEffect { TasksListSideEffect.Navigation.OpenAddTask }
            }

            is TasksListIntent.ChangeCategory -> {
                setState { copy(selectedCategory = event.category) }
            }

            is TasksListIntent.EditTask -> {
                updateTask(event.task)
                getAllTasks()
            }

            is TasksListIntent.openTaskDetails -> {
                setEffect { TasksListSideEffect.Navigation.OpenTaskDetails(event.task) }
            }

            is TasksListIntent.DeleteTask -> {
                deleteTask(event.task)
            }

            is TasksListIntent.RestoreTask -> {
                addTask(event.task)
                getAllTasks()
            }

            is TasksListIntent.openSettings -> {
                setEffect { TasksListSideEffect.Navigation.OpenSettings }

            }
             is TasksListIntent.SortedTasks -> {
               sortedTasks(event.sortingType)
            }
        }
    }

    private fun sortedTasks(sortingType: SortingType) {
        repository.getAllTasks(sortingType).stateIn(viewModelScope, SharingStarted.Lazily, emptyList()).let {
            setState {
                copy(allTasks = it)
            }
        }
    }


    fun getAllTasks(sortingType: SortingType=SortingType.DUEDATE){
         repository.getAllTasks(sortingType).stateIn(viewModelScope, SharingStarted.Lazily, emptyList()).let {
            setState {
                copy(allTasks = it)
            }
        }
    }


    fun getFilteredTasks(): Flow<List<Task>> {
        val currentState = viewState.value

        return (currentState.allTasks ?: flowOf(emptyList())).map { tasks ->
            when (currentState.selectedCategory) {
                TaskCategory.ALL -> tasks
                TaskCategory.COMPLETED -> tasks.filter { it.isCompleted }
                TaskCategory.PENDING -> tasks.filter { !it.isCompleted }
            }
        }
    }




    fun updateTask(task: Task) = viewModelScope.launch {
        repository.updateTask(task)

    }
    fun addTask(task: Task) = viewModelScope.launch {
        repository.addTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch { repository.deleteTask(task) }
}