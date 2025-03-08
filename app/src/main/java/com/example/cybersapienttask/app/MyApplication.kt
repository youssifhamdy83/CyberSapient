/**
 * Created by [Youssef Hamdy] on 3/5/2025.
 */
package com.example.cybersapienttask.app

import android.app.Application
import com.example.cybersapienttask.core.di.taskModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Start Koin DI
        startKoin {
            androidContext(this@MyApplication)
            modules(taskModule)
        }
    }
}