/**
 * Created by [Youssef Hamdy] on 3/6/2025.
 */
package com.example.matbakhna.data.repos

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.cybersapienttask.data.source.local.db.dao.TaskDao
import com.example.cybersapienttask.data.source.local.db.entities.SortingType
import com.example.cybersapienttask.data.source.local.db.entities.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {


    fun getAllTasks(sortingType: SortingType) :Flow<List<Task>> = taskDao.getAllTasks(getTasksSorted(sortingType))

    suspend fun addTask(task: Task) = taskDao.insertTask(task)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    fun getTaskTaskDetails(taskId: Int) = taskDao.getTaskDetails(taskId)
    fun getTasksSorted(sortOption: SortingType): SupportSQLiteQuery {
        val query = when (sortOption) {
            SortingType.PRIORITY -> "SELECT * FROM tasks ORDER BY priorityLevel ASC"
            SortingType.DUEDATE-> "SELECT * FROM tasks ORDER BY dueDate ASC"
            SortingType.ALPHABETICAL -> "SELECT * FROM tasks ORDER BY title ASC"
        }
        return SimpleSQLiteQuery(query)
    }
}