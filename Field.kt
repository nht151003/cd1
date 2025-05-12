package com.example.researchmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fields")
data class Field(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name: String,
    val description: String,
    val createdAt: Long = System.currentTimeMillis()
)
