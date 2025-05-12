package com.example.researchmanager.ui.documents

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.researchmanager.R
import com.example.researchmanager.data.DatabaseProvider
import com.example.researchmanager.repository.*
import com.example.researchmanager.ui.DocumentDetailActivity
import com.example.researchmanager.ui.adapter.DocumentAdapter
import com.example.researchmanager.viewmodel.DocumentViewModel
import com.example.researchmanager.viewmodel.ViewModelFactory

class DocumentListFragment : Fragment() {
    private lateinit var documentViewModel: DocumentViewModel
    private lateinit var documentAdapter: DocumentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_document_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)
        setupViewModel()
        setupRecyclerView()
        setupSearchListener()
    }

    private fun setupViews(view: View) {
        recyclerView = view.findViewById(R.id.rvDocuments)
        searchEditText = view.findViewById(R.id.etSearch)
    }

    private fun setupViewModel() {
        val db = DatabaseProvider.getDatabase(requireContext())
        val factory = ViewModelFactory(
            UserRepository(db.userDao()),
            FieldRepository(db.fieldDao()),
            DocumentRepository(db.documentDao()),
            NoteRepository(db.noteDao()),
            SharedDocumentRepository(db.sharedDocumentDao())
        )
        documentViewModel = ViewModelProvider(this, factory)[DocumentViewModel::class.java]
    }

    private fun setupRecyclerView() {
        documentAdapter = DocumentAdapter { document ->
            // Handle document click
            val intent = android.content.Intent(requireContext(), DocumentDetailActivity::class.java).apply {
                putExtra("doc_id", document.id)
                putExtra("user_id", document.userId)
            }
            startActivity(intent)
        }

        recyclerView.apply {
            adapter = documentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        documentViewModel.documents.observe(viewLifecycleOwner) { documents ->
            documentAdapter.submitList(documents)
        }
    }

    private fun setupSearchListener() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let { query ->
                    documentViewModel.search(query).observe(viewLifecycleOwner) { documents ->
                        documentAdapter.submitList(documents)
                    }
                }
            }
        })
    }
}