package com.example.researchmanager.repository

import com.example.researchmanager.data.dao.SharedDocumentDao
import com.example.researchmanager.data.model.SharedDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SharedDocumentRepository(
    private val sharedDocumentDao: SharedDocumentDao
) {
    fun getAllSharedDocuments(): Flow<List<SharedDocument>> = sharedDocumentDao.getAll()

    fun getSharedDocumentsByUser(userId: Int): Flow<List<SharedDocument>> {
        return if (userId > 0) {
            sharedDocumentDao.getByUser(userId)
        } else {
            flowOf(emptyList())
        }
    }

    fun getSharedDocumentsByDocument(documentId: Int): Flow<List<SharedDocument>> {
        return if (documentId > 0) {
            sharedDocumentDao.getByDocument(documentId)
        } else {
            flowOf(emptyList())
        }
    }

    suspend fun shareDocument(sharedDocument: SharedDocument) {
        if (sharedDocument.documentId > 0 && sharedDocument.userId > 0) {
            sharedDocumentDao.insert(sharedDocument)
        }
    }

    suspend fun unshareDocument(sharedDocument: SharedDocument) {
        if (sharedDocument.id > 0) {
            sharedDocumentDao.delete(sharedDocument)
        }
    }
}
