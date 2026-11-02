package com.trackademia.app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val parsedDate: Long? = null,
    val hasReminder: Boolean = false,
    val syncStatus: String = "PENDING"
)