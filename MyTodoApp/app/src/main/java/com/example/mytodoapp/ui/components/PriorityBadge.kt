package com.example.mytodoapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytodoapp.domain.model.Priority
import com.example.mytodoapp.ui.theme.*

@Composable
fun PriorityBadge(priority: Priority, modifier: Modifier = Modifier) {
    val (bgColor, textColor, label) = when (priority) {
        Priority.URGENT -> Triple(PriorityUrgentContainer, PriorityUrgent, "Urgent")
        Priority.HIGH   -> Triple(PriorityHighContainer, PriorityHigh, "High")
        Priority.MEDIUM -> Triple(PriorityMediumContainer, PriorityMedium, "Medium")
        Priority.LOW    -> Triple(PriorityLowContainer, PriorityLow, "Low")
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            style = MaterialTheme.typography.labelSmall
        )
    }
}