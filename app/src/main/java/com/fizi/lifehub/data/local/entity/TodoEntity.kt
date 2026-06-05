package com.fizi.lifehub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val isDone: Boolean = false,
    val priority: Int = 1, // 1=Low, 2=Medium, 3=High
    val createdAt: Long = System.currentTimeMillis()
)
