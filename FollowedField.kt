package com.example.researchmanager.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "followed_fields",
    foreignKeys = [
        ForeignKey(
            entity = Field::class,
            parentColumns = ["id"],
            childColumns = ["fieldId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FollowedField(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val fieldId: Int,
    val userId: Int,
    val createdAt: Long = System.currentTimeMillis()
)
