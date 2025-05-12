package com.example.researchmanager.repository

import com.example.researchmanager.data.dao.NoteDao
import com.example.researchmanager.data.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NoteRepository(
    private val noteDao: NoteDao
) {
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAll()

    fun getNoteById(id: Int): Flow<Note?> {
        return if (id > 0) {
            noteDao.getById(id)
        } else {
            flowOf(null)
        }
    }

    fun getNotesByDocument(documentId: Int): Flow<List<Note>> {
        return if (documentId > 0) {
            noteDao.getByDocument(documentId)
        } else {
            flowOf(emptyList())
        }
    }

    suspend fun insertNote(note: Note): Long {
        return (if (note.content.isNotBlank() && note.documentId > 0) {
            noteDao.insert(note)
        } else {
            -1L
        }) as Long
    }

    suspend fun updateNote(note: Note) {
        if (note.id > 0 && note.content.isNotBlank()) {
            noteDao.update(note)
        }
    }

    suspend fun deleteNote(note: Note) {
        if (note.id > 0) {
            noteDao.delete(note)
        }
    }


suspend fun deleteNoteById(id: Int) {
    if (id > 0) {
        try {
            noteDao.deleteById(id)
            // Có thể thêm logic thông báo thành công ở đây nếu cần
        } catch (e: Exception) {
            // Log lỗi hoặc xử lý lỗi khác
            println("Error deleting note with id $id: ${e.message}")
            // Có thể throw lại exception hoặc trả về một giá trị lỗi
        }
    } else {
        println("Attempted to delete note with invalid id: $id")
    }
}
}