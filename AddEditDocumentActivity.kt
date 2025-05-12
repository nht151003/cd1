package com.example.researchmanager.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.researchmanager.data.model.Document
import com.example.researchmanager.databinding.ActivityAddEditDocumentBinding
import com.example.researchmanager.viewmodel.DocumentViewModel
import com.example.researchmanager.viewmodel.FieldViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddEditDocumentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditDocumentBinding
    private lateinit var documentViewModel: DocumentViewModel
    private lateinit var fieldViewModel: FieldViewModel

    private var selectedFieldId = 0
    private var isEditMode = false
    private var editingDocId = 0
    private var currentUserId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        documentViewModel = ViewModelProvider(this)[DocumentViewModel::class.java]
        fieldViewModel = ViewModelProvider(this)[FieldViewModel::class.java]

        currentUserId = intent.getIntExtra("user_id", -1)
        editingDocId = intent.getIntExtra("doc_id", 0)
        isEditMode = editingDocId != 0

        setupViews()
        loadFields()
        if (isEditMode) {
            loadDocumentDetails()
        }
    }

    private fun setupViews() {
        binding.btnSave.setOnClickListener {
            saveDocument()
        }
    }

    private fun loadFields() {
        lifecycleScope.launch {
            try {
                fieldViewModel.getAllFields().collect { fields ->
                    val fieldNames = fields.map { it.name }

                    val adapter = ArrayAdapter(
                        this@AddEditDocumentActivity,
                        android.R.layout.simple_spinner_item,
                        fieldNames
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerField.setAdapter(adapter)

                    binding.spinnerField.setOnItemClickListener { _, _, position, _ ->
                        selectedFieldId = fields[position].id
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@AddEditDocumentActivity,
                    "Lỗi khi tải danh sách lĩnh vực", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadDocumentDetails() {
        lifecycleScope.launch {
            try {
                val document = documentViewModel.getById(editingDocId)
                document?.let { doc ->
                    binding.etTitle.setText(doc.title)
                    binding.etAuthor.setText(doc.author)
                    binding.etContent.setText(doc.content)

                    val field = fieldViewModel.getById(doc.fieldId)
                    field?.let { f ->
                        binding.spinnerField.setText(f.name, false)
                        selectedFieldId = f.id
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@AddEditDocumentActivity,
                    "Lỗi khi tải thông tin tài liệu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDocument() {
        val title = binding.etTitle.text.toString()
        val author = binding.etAuthor.text.toString()
        val content = binding.etContent.text.toString()
        val fieldName = binding.spinnerField.text.toString()

        if (title.isBlank() || author.isBlank() || fieldName.isBlank()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val fields = fieldViewModel.getAllFields().first()
                val field = fields.firstOrNull { it.name == fieldName }

                if (field != null) {
                    val document = Document(
                        id = if (isEditMode) editingDocId else 0,
                        title = title,
                        author = author,
                        content = content,
                        fieldId = field.id,
                        userId = currentUserId
                    )

                    if (isEditMode) {
                        documentViewModel.update(document)
                    } else {
                        documentViewModel.insert(document)
                    }

                    Toast.makeText(this@AddEditDocumentActivity,
                        "Đã lưu tài liệu", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddEditDocumentActivity,
                        "Vui lòng chọn lĩnh vực hợp lệ", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AddEditDocumentActivity,
                    "Lỗi khi lưu tài liệu", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
