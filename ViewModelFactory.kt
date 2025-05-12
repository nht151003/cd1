package com.example.researchmanager.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


import com.example.researchmanager.repository.UserRepository
import com.example.researchmanager.viewmodel.UserViewModel

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding

    // Initialize repositories manually (or pass them from somewhere else)
    private val userRepository by lazy { UserRepository() }
    private val fieldRepository by lazy { FieldRepository() }
    private val documentRepository by lazy { DocumentRepository() }
    private val noteRepository by lazy { NoteRepository() }
    private val sharedDocumentRepository by lazy { SharedDocumentRepository() }

    // Use the custom ViewModelFactory to instantiate the ViewModel
    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory(userRepository, fieldRepository, documentRepository, noteRepository, sharedDocumentRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Use the userViewModel to observe data or perform actions
    }
}
