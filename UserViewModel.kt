package com.example.researchmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.researchmanager.data.model.User
import com.example.researchmanager.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow(User())
    val currentUser: StateFlow<User> = _currentUser

    fun login(username: String, password: String) = viewModelScope.launch {
        userRepository.login(username, password)?.let { user ->
            _currentUser.value = user
        }
    }

    fun register(username: String, password: String) = viewModelScope.launch {
        userRepository.register(username, password)
    }

    fun logout() = viewModelScope.launch {
        userRepository.logout()
    }

    fun getCurrentUser() = viewModelScope.launch {
        userRepository.getCurrentUser().collect { user ->
            _currentUser.value = user
        }
    }
}
