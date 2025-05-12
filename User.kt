package com.example.researchmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val username: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
)
