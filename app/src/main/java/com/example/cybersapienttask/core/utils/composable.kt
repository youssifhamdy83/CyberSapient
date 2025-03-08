/**
 * Created by [Youssef Hamdy] on 3/6/2025.
 */
package com.example.cybersapienttask.core.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

    @Composable
    fun TaskCompleteCircle(modifier: Modifier ,isCompleted: Boolean, onClick: (Boolean) -> Unit,size:Int) {

        Box(contentAlignment = Alignment.Center,
            modifier = modifier
                .size(size.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
                .background(if (isCompleted) MaterialTheme.colorScheme.primary else Color.Transparent)
                .clickable { onClick(!isCompleted) }) {
            if (isCompleted) {
                Icon(Icons.Filled.Done, contentDescription = "Complete task", tint = Color.White)
            }
        }
    }