package com.example.researchmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.researchmanager.data.model.Document
import com.example.researchmanager.repository.DocumentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class DocumentViewModel(private val documentRepository: DocumentRepository) : ViewModel() {

    private val _documents = MutableStateFlow<List<Document>>(emptyList())
    val documents: StateFlow<List<Document>> = _documents

    private val _currentDocument = MutableStateFlow<Document>(Document())
    val currentDocument: StateFlow<Document> = _currentDocument

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadDocuments()
    }

    private fun loadDocuments() = viewModelScope.launch {
        _isLoading.value = true
        documentRepository.getAllDocuments()
            .catch { exception ->
                _error.value = "Error loading documents: ${exception.message}"
            }
            .collect { documentList ->
                _documents.value = documentList
                _isLoading.value = false
            }
    }

    fun getDocumentById(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        documentRepository.getDocumentById(id)
            .catch { exception ->
                _error.value = "Error loading document: ${exception.message}"
            }
            .collect { document ->
                _currentDocument.value = document
                _isLoading.value = false
            }
    }

    fun addDocument(document: Document) = viewModelScope.launch {
        _isLoading.value = true
        try {
            documentRepository.addDocument(document)
            loadDocuments() // reload the documents list after adding a new one
        } catch (e: Exception) {
            _error.value = "Error adding document: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun updateDocument(document: Document) = viewModelScope.launch {
        _isLoading.value = true
        try {
            documentRepository.updateDocument(document)
            loadDocuments() // reload the documents list after update
        } catch (e: Exception) {
            _error.value = "Error updating document: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun deleteDocument(document: Document) = viewModelScope.launch {
        _isLoading.value = true
        try {
            documentRepository.deleteDocument(document)
            _currentDocument.value = Document() // reset current document
            loadDocuments() // reload the documents list after deletion
        } catch (e: Exception) {
            _error.value = "Error deleting document: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun searchDocuments(query: String) = viewModelScope.launch {
        _isLoading.value = true
        try {
            documentRepository.searchDocuments(query)
                .catch { exception ->
                    _error.value = "Error searching documents: ${exception.message}"
                }
                .collect { documentList ->
                    _documents.value = documentList
                    _isLoading.value = false
                }
        } catch (e: Exception) {
            _error.value = "Error searching documents: ${e.message}"
            _isLoading.value = false
        }
    }

    fun getDocumentsByField(fieldId: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            documentRepository.getDocumentsByField(fieldId)
                .catch { exception ->
                    _error.value = "Error loading documents by field: ${exception.message}"
                }
                .collect { documentList ->
                    _documents.value = documentList
                    _isLoading.value = false
                }
        } catch (e: Exception) {
            _error.value = "Error loading documents by field: ${e.message}"
            _isLoading.value = false
        }
    }
}
