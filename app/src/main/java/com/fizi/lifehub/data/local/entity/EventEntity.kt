package com.fizi.lifehub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val color: Long = 0xFF00897B,
    val isAllDay: Boolean = false,
    val reminder: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
