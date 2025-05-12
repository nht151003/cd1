package com.example.researchmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.researchmanager.data.model.SharedDocument
import com.example.researchmanager.repository.SharedDocumentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedDocumentViewModel(private val sharedDocumentRepository: SharedDocumentRepository) : ViewModel() {

    private val _sharedDocuments = MutableStateFlow<List<SharedDocument>>(emptyList())
    val sharedDocuments: StateFlow<List<SharedDocument>> = _sharedDocuments

    init {
        loadSharedDocuments()
    }

    private fun loadSharedDocuments() = viewModelScope.launch {
        sharedDocumentRepository.getAllSharedDocuments().collect { sharedDocumentList ->
            _sharedDocuments.value = sharedDocumentList
        }
    }

    fun shareDocument(sharedDocument: SharedDocument) = viewModelScope.launch {
        sharedDocumentRepository.shareDocument(sharedDocument)
    }

    fun unshareDocument(sharedDocument: SharedDocument) = viewModelScope.launch {
        sharedDocumentRepository.unshareDocument(sharedDocument)
    }

    fun getSharedDocumentsByUser(userId: Int) = viewModelScope.launch {
        sharedDocumentRepository.getSharedDocumentsByUser(userId).collect { sharedDocumentList ->
            _sharedDocuments.value = sharedDocumentList
        }
    }

    fun getSharedDocumentsByDocument(documentId: Int) = viewModelScope.launch {
        sharedDocumentRepository.getSharedDocumentsByDocument(documentId).collect { sharedDocumentList ->
            _sharedDocuments.value = sharedDocumentList
        }
    }
}
