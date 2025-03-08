/**
 * Created by [Youssef Hamdy] on 3/5/2025.
 */
package com.example.cybersapienttask.data.source.local.db
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cybersapienttask.data.source.local.db.dao.TaskDao
import com.example.cybersapienttask.data.source.local.db.entities.Task


@Database(entities = [Task::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun taskDao(): TaskDao
}