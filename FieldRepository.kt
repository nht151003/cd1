package com.example.researchmanager.repository

import com.example.researchmanager.data.dao.FieldDao
import com.example.researchmanager.data.model.Field
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FieldRepository(
    private val fieldDao: FieldDao
) {
    fun getAllFields(): Flow<List<Field>> = fieldDao.getAll()

    fun getFieldById(id: Int): Flow<Field?> {
        return if (id > 0) {
            fieldDao.getById(id)
        } else {
            flowOf(null)
        }
    }

    suspend fun addField(field: Field) {
        if (field.name.isNotBlank() && field.description.isNotBlank()) {
            fieldDao.insert(field)
        }
    }

    suspend fun updateField(field: Field) {
        if (field.id > 0 && field.name.isNotBlank() && field.description.isNotBlank()) {
            fieldDao.update(field)
        }
    }

    suspend fun deleteField(field: Field) {
        if (field.id > 0) {
            fieldDao.delete(field)
        }
    }
}
