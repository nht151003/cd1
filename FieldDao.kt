package com.example.researchmanager.data.dao

import androidx.room.*
import com.example.researchmanager.data.model.Field
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldDao {
    @Query("SELECT * FROM fields")
    fun getAll(): Flow<List<Field>>

    @Query("SELECT * FROM fields WHERE id = :id")
    fun getById(id: Int): Flow<Field?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(field: Field)

    @Update
    suspend fun update(field: Field)

    @Delete
    suspend fun delete(field: Field)
}
