package com.example.researchmanager.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.researchmanager.databinding.ActivityMainBinding
import com.example.researchmanager.ui.adapter.DocumentAdapter
import com.example.researchmanager.viewmodel.DocumentViewModel
import com.example.researchmanager.viewmodel.FollowedFieldViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val documentViewModel: DocumentViewModel by viewModels()
    private val followedFieldViewModel: FollowedFieldViewModel by viewModels()

    private lateinit var documentAdapter: DocumentAdapter
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUserId = intent.getIntExtra("user_id", -1)
        documentViewModel.setCurrentUserId(currentUserId)

        setupRecyclerViews()
        setupFab()
        observeData()
    }

    private fun setupRecyclerViews() {
        // Setup RecyclerView for the main document list
        documentAdapter = DocumentAdapter { doc -> navigateToDocumentDetail(doc.id) }
        setupRecyclerView(binding.rvDocuments, documentAdapter)

        // Setup RecyclerView for the followed document list
        val followedDocsAdapter = DocumentAdapter { doc -> navigateToDocumentDetail(doc.id) }
        setupRecyclerView(binding.rvFollowedDocuments, followedDocsAdapter)

        // Setup RecyclerView for the recent document list
        val recentDocsAdapter = DocumentAdapter { doc -> navigateToDocumentDetail(doc.id) }
        setupRecyclerView(binding.rvRecentDocuments, recentDocsAdapter)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: DocumentAdapter) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditDocumentActivity::class.java).apply {
                putExtra("user_id", currentUserId)
            }
            startActivity(intent)
        }
    }

    private fun observeData() {
        // Observe main documents list
        documentViewModel.documents.observe(this) { docs ->
            documentAdapter.submitList(docs)
            if (docs.isEmpty()) showErrorMessage("No documents found.")
        }

        // Observe followed fields and their documents
        followedFieldViewModel.getFollowedFieldIds(currentUserId).observe(this) { fieldIds ->
            documentViewModel.getDocumentsByFollowedFields(fieldIds).observe(this) { docs ->
                documentAdapter.submitList(docs)
                if (docs.isEmpty()) showErrorMessage("No followed documents found.")
            }
        }

        // Observe recent documents
        documentViewModel.getRecentDocuments().observe(this) { recentDocs ->
            documentAdapter.submitList(recentDocs)
            if (recentDocs.isEmpty()) showErrorMessage("No recent documents found.")
        }
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToDocumentDetail(docId: Int) {
        val intent = Intent(this, DocumentDetailActivity::class.java).apply {
            putExtra("doc_id", docId)
            putExtra("user_id", currentUserId)
        }
        startActivity(intent)
    }
}
