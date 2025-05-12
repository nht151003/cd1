package com.example.researchmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.researchmanager.data.model.FollowedField
import com.example.researchmanager.repository.FollowedFieldRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FollowedFieldViewModel(private val followedFieldRepository: FollowedFieldRepository) : ViewModel() {

    private val _followedFields = MutableStateFlow<List<FollowedField>>(emptyList())
    val followedFields: StateFlow<List<FollowedField>> = _followedFields

    fun getFollowedFieldsByUser(userId: Int) = viewModelScope.launch {
        followedFieldRepository.getFollowedFieldsByUser(userId).collect { followedFieldList ->
            _followedFields.value = followedFieldList
        }
    }

    fun followField(followedField: FollowedField) = viewModelScope.launch {
        followedFieldRepository.followField(followedField)
    }

    fun unfollowField(followedField: FollowedField) = viewModelScope.launch {
        followedFieldRepository.unfollowField(followedField)
    }
}
