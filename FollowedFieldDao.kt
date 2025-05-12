package com.example.researchmanager.data.dao

import androidx.room.*
import com.example.researchmanager.data.model.FollowedField
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowedFieldDao {
    @Query("SELECT * FROM followed_fields WHERE userId = :userId")
    fun getByUser(userId: Int): Flow<List<FollowedField>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(followedField: FollowedField)

    @Delete
    suspend fun delete(followedField: FollowedField)
    fun isFollowingField(i: Int, fieldId: Int): Boolean
}
