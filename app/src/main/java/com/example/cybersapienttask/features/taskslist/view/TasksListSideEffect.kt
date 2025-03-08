/**
 * Created by [Youssef Hamdy] on 3/5/2025.
 */
package com.example.cybersapienttask.features.taskslist.view

import com.example.cybersapienttask.core.base.ViewSideEffect
import com.example.cybersapienttask.data.source.local.db.entities.Task


sealed class TasksListSideEffect : ViewSideEffect {

    sealed class Navigation : TasksListSideEffect() {
        data class OpenTaskDetails(val task: Task) : Navigation()
        data object OpenAddTask : Navigation()
        data object OpenSettings : Navigation()
    }

}