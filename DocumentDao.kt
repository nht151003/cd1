package com.example.researchmanager.data.dao

import androidx.room.*
import com.example.researchmanager.data.model.Document
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents")
    fun getAll(): Flow<List<Document>>

    @Query("SELECT * FROM documents WHERE id = :id")
    fun getById(id: Int): Flow<Document?>

    @Query("SELECT * FROM documents WHERE userId = :userId")
    fun getByUserId(userId: Int): Flow<List<Document>>

    @Query("SELECT * FROM documents WHERE fieldId = :fieldId")
    fun getByField(fieldId: Int): Flow<List<Document>>

    @Query("SELECT * FROM documents WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<Document>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(document: Document)

    @Update
    suspend fun update(document: Document)

    @Delete
    suspend fun delete(document: Document)

    @Query("DELETE FROM documents WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM documents WHERE userId = :userId")
    fun getByUser(userId: Int): Flow<List<Document>>

    @Query("SELECT * FROM documents WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    fun searchDocuments(query: String): Flow<List<Document>>

    @Query("SELECT * FROM documents WHERE fieldId IN (:fieldIds) ORDER BY createdAt DESC")
    fun getDocumentsByFollowedFields(fieldIds: List<Int>): Flow<List<Document>>

    @Query("SELECT * FROM documents ORDER BY createdAt DESC LIMIT 20")
    fun getRecentDocuments(): Flow<List<Document>>
}
