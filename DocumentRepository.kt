package com.example.researchmanager.repository

import com.example.researchmanager.data.dao.DocumentDao
import com.example.researchmanager.data.model.Document
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DocumentRepository(
    private val documentDao: DocumentDao
) {
    fun getAllDocuments(): Flow<List<Document>> = documentDao.getAll()

    fun getDocumentById(id: Int): Flow<Document?> = documentDao.getById(id)

    fun searchDocuments(query: String): Flow<List<Document>> {
        return if (query.isBlank()) {
            documentDao.getAll()
        } else {
            documentDao.search(query)
        }
    }

    fun getDocumentsByField(fieldId: Int): Flow<List<Document>> {
        return if (fieldId > 0) {
            documentDao.getByField(fieldId)
        } else {
            kotlinx.coroutines.flow.flowOf(emptyList())
        }
    }

    suspend fun addDocument(document: Document) {
        if (document.title.isNotBlank() && document.content.isNotBlank()) {
            documentDao.insert(document)
        }
    }

    suspend fun updateDocument(document: Document) {
        if (document.id > 0 && document.title.isNotBlank() && document.content.isNotBlank()) {
            documentDao.update(document)
        }
    }

    suspend fun deleteDocument(document: Document) {
        if (document.id > 0) {
            documentDao.delete(document)
        }
    }
}
