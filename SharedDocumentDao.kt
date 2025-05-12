package com.example.researchmanager.data.dao

import androidx.room.*
import com.example.researchmanager.data.model.SharedDocument
import kotlinx.coroutines.flow.Flow

@Dao
interface SharedDocumentDao {
    @Query("SELECT * FROM shared_documents")
    fun getAll(): Flow<List<SharedDocument>>

    @Query("SELECT * FROM shared_documents WHERE userId = :userId")
    fun getByUser(userId: Int): Flow<List<SharedDocument>>

    @Query("SELECT * FROM shared_documents WHERE documentId = :documentId")
    fun getByDocument(documentId: Int): Flow<List<SharedDocument>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sharedDocument: SharedDocument)

    @Delete
    suspend fun delete(sharedDocument: SharedDocument)
}
