package com.example.researchmanager.data.dao

import androidx.room.*
import com.example.researchmanager.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    fun getById(id: Int): Flow<User?>

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getByUsername(username: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}
