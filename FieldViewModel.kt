package com.example.researchmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.researchmanager.data.model.Field
import com.example.researchmanager.repository.FieldRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FieldViewModel(private val fieldRepository: FieldRepository) : ViewModel() {

    private val _fields = MutableStateFlow<List<Field>>(emptyList())
    val fields: StateFlow<List<Field>> = _fields

    init {
        loadFields()
    }

    private fun loadFields() = viewModelScope.launch {
        fieldRepository.getAllFields().collect { fieldList ->
            _fields.value = fieldList
        }
    }

    fun addField(field: Field) = viewModelScope.launch {
        fieldRepository.addField(field)
    }

    fun updateField(field: Field) = viewModelScope.launch {
        fieldRepository.updateField(field)
    }

    fun deleteField(field: Field) = viewModelScope.launch {
        fieldRepository.deleteField(field)
    }
}
