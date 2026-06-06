package com.fizi.lifehub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GoalStatus { IN_PROGRESS, COMPLETED, ABANDONED }

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val emoji: String = "🎯",
    val targetDate: String = "",
    val progress: Int = 0,
    val status: GoalStatus = GoalStatus.IN_PROGRESS,
    val createdAt: Long = System.currentTimeMillis()
)
