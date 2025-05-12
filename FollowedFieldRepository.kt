package com.example.researchmanager.repository

import com.example.researchmanager.data.dao.FollowedFieldDao
import com.example.researchmanager.data.model.FollowedField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FollowedFieldRepository(
    private val followedFieldDao: FollowedFieldDao
) {
    fun getFollowedFieldsByUser(userId: Int): Flow<List<FollowedField>> {
        return if (userId > 0) {
            followedFieldDao.getByUser(userId)
        } else {
            flowOf(emptyList())
        }
    }

    suspend fun followField(followedField: FollowedField) {
        if (followedField.userId > 0 && followedField.fieldId > 0) {
            followedFieldDao.insert(followedField)
        }
    }

    suspend fun unfollowField(followedField: FollowedField) {
        if (followedField.id > 0) {
            followedFieldDao.delete(followedField)
        }
    }

    suspend fun isFollowingField(userId: Int, fieldId: Int): Boolean {
        return if (userId > 0 && fieldId > 0) {
            followedFieldDao.isFollowingField(userId, fieldId)
        } else {
            false
        }
    }
}
