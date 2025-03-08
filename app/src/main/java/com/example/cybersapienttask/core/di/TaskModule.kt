/**
 * Created by [Youssef Hamdy] on 3/5/2025.
 */
package com.example.cybersapienttask.core.di

import androidx.room.Room
import com.example.cybersapienttask.data.source.local.db.AppDatabase
import com.example.cybersapienttask.features.addtask.viewmodel.AddTaskViewModel
import com.example.cybersapienttask.features.taskslist.viewmodel.TaskViewModel
import com.example.cybersapienttask.features.taskdetails.viewmodel.TaskDetailsViewModel
import com.example.matbakhna.data.repos.TaskRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val taskModule = module {

    // Provide Room Database
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    // Provide TaskDao
    single { get<AppDatabase>().taskDao() } // singleton

    // Provide TaskRepository with TaskDao
    single { TaskRepository(get()) } // singleton

    // Provide TaskViewModel
    viewModel { TaskViewModel(get()) }
    viewModel { TaskDetailsViewModel(get()) }
    viewModel { AddTaskViewModel(get()) }


}