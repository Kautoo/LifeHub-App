package com.fizi.lifehub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_logs")
data class WaterLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String = "",
    val glasses: Int = 0,
    val target: Int = 8,
    val createdAt: Long = System.currentTimeMillis()
)
