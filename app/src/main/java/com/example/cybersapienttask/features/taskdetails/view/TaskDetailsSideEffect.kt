/**
 * Created by [Youssef Hamdy] on 3/8/2025.
 */
package com.example.cybersapienttask.features.taskdetails.view

import com.example.cybersapienttask.core.base.ViewSideEffect
import com.example.cybersapienttask.data.source.local.db.entities.Task


sealed class TaskDetailsSideEffect : ViewSideEffect {


    sealed class Navigation : TaskDetailsSideEffect(){
        object BackToTaskList: Navigation()

    }

}