/**
 * Created by [Youssef Hamdy] on 3/6/2025.
 */
package com.example.cybersapienttask.features.addtask.view

import com.example.cybersapienttask.core.base.ViewSideEffect
import com.example.cybersapienttask.data.source.local.db.entities.Task


sealed class AddTaskSideEffect : ViewSideEffect {
    sealed class Navigation : AddTaskSideEffect() {
        object BackToTaskList : Navigation()

    }

}