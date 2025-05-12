package com.example.researchmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.researchmanager.data.model.Note
import com.example.researchmanager.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _currentNote = MutableStateFlow(Note())
    val currentNote: StateFlow<Note> = _currentNote

    init {
        loadNotes()
    }

    private fun loadNotes() = viewModelScope.launch {
        noteRepository.getAllNotes().collect { noteList ->
            _notes.value = noteList
        }
    }

    fun getNoteById(id: Int) = viewModelScope.launch {
        noteRepository.getNoteById(id).collect { note ->
            _currentNote.value = note
        }
    }

    fun addNote(note: Note) = viewModelScope.launch {
        noteRepository.addNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        noteRepository.updateNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        noteRepository.deleteNote(note)
        _currentNote.value = Note()
    }

    fun getNotesByDocument(documentId: Int) = viewModelScope.launch {
        noteRepository.getNotesByDocument(documentId).collect { noteList ->
            _notes.value = noteList
        }
    }
}
