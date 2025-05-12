package com.example.researchmanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.researchmanager.R
import com.example.researchmanager.data.model.Document
import com.example.researchmanager.databinding.ActivityDocumentDetailBinding
import com.example.researchmanager.viewmodel.DocumentViewModel
import com.example.researchmanager.viewmodel.FieldViewModel
import com.example.researchmanager.viewmodel.SharedDocumentViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DocumentDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDocumentDetailBinding
    private val documentViewModel: DocumentViewModel by viewModels()
    private val fieldViewModel: FieldViewModel by viewModels()
    private val sharedDocumentViewModel: SharedDocumentViewModel by viewModels()

    private var documentId: Int = -1
    private var isEditMode = false
    private var selectedFieldId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        documentId = intent.getIntExtra(EXTRA_DOCUMENT_ID, -1)
        isEditMode = intent.getBooleanExtra(EXTRA_EDIT_MODE, false)

        setupToolbar()
        setupViews()
        loadDocumentDetails()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = if (isEditMode) "Chỉnh sửa tài liệu" else "Chi tiết tài liệu"
        }
    }

    private fun setupViews() {
        binding.apply {
            // Enable/disable editing based on mode
            etTitle.isEnabled = isEditMode
            etAuthor.isEnabled = isEditMode
            spinnerField.isEnabled = isEditMode
            etContent.isEnabled = isEditMode

            // Setup field spinner
            lifecycleScope.launch {
                try {
                    fieldViewModel.getAllFields().collect { fields ->
                        val fieldNames = fields.map { it.name }
                        val adapter = ArrayAdapter(this@DocumentDetailActivity,
                            android.R.layout.simple_spinner_item, fieldNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerField.setAdapter(adapter)

                        spinnerField.setOnItemClickListener { _, _, position, _ ->
                            selectedFieldId = fields[position].id
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@DocumentDetailActivity,
                        "Lỗi khi tải danh sách lĩnh vực", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadDocumentDetails() {
        if (documentId != -1) {
            lifecycleScope.launch {
                try {
                    val document = documentViewModel.getById(documentId)
                    document?.let { doc ->
                        binding.apply {
                            etTitle.setText(doc.title)
                            etAuthor.setText(doc.author)
                            etContent.setText(doc.content)

                            // Get field name and set it in spinner
                            val field = fieldViewModel.getById(doc.fieldId)
                            field?.let { f ->
                                spinnerField.setText(f.name, false)
                                selectedFieldId = f.id
                            }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@DocumentDetailActivity,
                        "Lỗi khi tải thông tin tài liệu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_document_detail, menu)
        // Show/hide menu items based on edit mode
        menu.findItem(R.id.action_edit)?.isVisible = !isEditMode
        menu.findItem(R.id.action_save)?.isVisible = isEditMode
        menu.findItem(R.id.action_delete)?.isVisible = !isEditMode
        menu.findItem(R.id.action_share)?.isVisible = !isEditMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_edit -> {
                startEditMode()
                true
            }
            R.id.action_save -> {
                saveDocument()
                true
            }
            R.id.action_delete -> {
                showDeleteConfirmation()
                true
            }
            R.id.action_share -> {
                showShareDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startEditMode() {
        isEditMode = true
        binding.apply {
            etTitle.isEnabled = true
            etAuthor.isEnabled = true
            spinnerField.isEnabled = true
            etContent.isEnabled = true
        }
        supportActionBar?.title = "Chỉnh sửa tài liệu"
        invalidateOptionsMenu()
    }

    private fun saveDocument() {
        val title = binding.etTitle.text.toString()
        val author = binding.etAuthor.text.toString()
        val content = binding.etContent.text.toString()
        val fieldName = binding.spinnerField.text.toString()

        if (title.isBlank() || author.isBlank() || content.isBlank() || fieldName.isBlank()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val field = fieldViewModel.getAllFields().firstOrNull { it.name == fieldName }
                if (field != null) {
                    val document = Document(
                        id = documentId,
                        title = title,
                        author = author,
                        content = content,
                        fieldId = field.id
                    )
                    documentViewModel.update(document)
                    Toast.makeText(this@DocumentDetailActivity, "Đã lưu tài liệu", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@DocumentDetailActivity,
                        "Không tìm thấy lĩnh vực", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DocumentDetailActivity,
                    "Lỗi khi lưu tài liệu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Xóa tài liệu")
            .setMessage("Bạn có chắc chắn muốn xóa tài liệu này?")
            .setPositiveButton("Xóa") { _, _ ->
                lifecycleScope.launch {
                    try {
                        documentViewModel.deleteById(documentId)
                        Toast.makeText(this@DocumentDetailActivity, "Đã xóa tài liệu", Toast.LENGTH_SHORT).show()
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(this@DocumentDetailActivity,
                            "Lỗi khi xóa tài liệu", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showShareDialog() {
        val editText = TextInputEditText(this).apply {
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(32, 0, 32, 0)
            }
            hint = "Nhập ID người dùng"
        }

        AlertDialog.Builder(this)
            .setTitle("Chia sẻ tài liệu")
            .setView(editText)
            .setPositiveButton("Chia sẻ") { _, _ ->
                val userId = editText.text.toString()
                if (userId.isNotBlank()) {
                    lifecycleScope.launch {
                        try {
                            sharedDocumentViewModel.shareDocument(documentId, userId.toInt())
                            Toast.makeText(this@DocumentDetailActivity,
                                "Đã chia sẻ tài liệu", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@DocumentDetailActivity,
                                "Lỗi khi chia sẻ tài liệu", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Vui lòng nhập ID người dùng", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    companion object {
        const val EXTRA_DOCUMENT_ID = "extra_document_id"
        const val EXTRA_EDIT_MODE = "extra_edit_mode"
    }
}
