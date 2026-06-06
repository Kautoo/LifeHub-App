package com.fizi.lifehub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Mood { HAPPY, NEUTRAL, SAD, ANGRY, EXCITED }

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String = "",
    val content: String = "",
    val mood: Mood = Mood.NEUTRAL,
    val tags: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
