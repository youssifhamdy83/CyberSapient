/**
 * Created by [Youssef Hamdy] on 3/5/2025.
 */
package com.example.cybersapienttask.data.source.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tasks")
data class Task(

    @PrimaryKey(autoGenerate = true) val
    id: Int = 0,
    val title: String,
    val description: String? = null,
    val priority: Priority,
    val priorityLevel: Int,
    val dueDate: String,
    var isCompleted: Boolean = false,
    var progress: Int = 0

)

enum class Priority(val value: Int) {
    HIGH(1),
    MEDIUM(2),
    LOW(3);
}
enum class TaskCategory { ALL, COMPLETED, PENDING }
enum class SortingType { PRIORITY, DUEDATE, ALPHABETICAL }

