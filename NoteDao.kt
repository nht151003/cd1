package com.example.researchmanager.data.dao

import androidx.room.*
import com.example.researchmanager.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    fun getAll(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getById(id: Int): Flow<Note?>

    @Query("SELECT * FROM notes WHERE documentId = :documentId")
    fun getByDocument(documentId: Int): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
    fun deleteById(i: Int)
}
