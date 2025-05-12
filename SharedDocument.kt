package com.example.researchmanager.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "shared_documents",
    foreignKeys = [
        ForeignKey(
            entity = Document::class,
            parentColumns = ["id"],
            childColumns = ["documentId"],
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
data class SharedDocument(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val documentId: Int,
    val userId: Int,
    val createdAt: Long = System.currentTimeMillis()
)
