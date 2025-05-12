package com.example.researchmanager.repository

import com.example.researchmanager.data.dao.UserDao
import com.example.researchmanager.data.model.User
import com.example.researchmanager.utils.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserRepository(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) {
    fun getCurrentUser(): Flow<User?> {
        val userId = sessionManager.getUserId()
        return if (userId != -1) {
            userDao.getById(userId)
        } else {
            flowOf(null)
        }
    }

    suspend fun login(username: String, password: String): User? {
        if (username.isBlank() || password.isBlank()) {
            return null
        }
        val user = userDao.getByUsername(username)
        if (user != null && user.password == password) {
            sessionManager.saveUserId(user.id)
            sessionManager.saveUsername(user.username)
            sessionManager.setLoggedIn(true)
            return user
        }
        return null
    }

    suspend fun register(username: String, password: String): User? {
        if (username.isBlank() || password.isBlank()) {
            return null
        }
        val existingUser = userDao.getByUsername(username)
        if (existingUser != null) {
            return null
        }
        val user = User(username = username, password = password)
        val userId = userDao.insert(user).toInt()
        user.id = userId
        return user
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }
}
