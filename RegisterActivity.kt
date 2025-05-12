package com.example.researchmanager.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.researchmanager.data.model.User
import com.example.researchmanager.databinding.ActivityRegisterBinding
import com.example.researchmanager.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            btnRegister.setOnClickListener {
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                if (username.isBlank() || password.isBlank()) {
                    showToast("Vui lòng nhập đủ thông tin")
                    return@setOnClickListener
                }

                if (!isPasswordValid(password)) {
                    showToast("Mật khẩu phải có ít nhất 6 ký tự")
                    return@setOnClickListener
                }

                val newUser = User(username = username, password = password)
                userViewModel.register(newUser) { success ->
                    runOnUiThread {
                        if (success) {
                            showToast("Đăng ký thành công")
                            clearFields()
                            finish()
                        } else {
                            showToast("Tài khoản đã tồn tại")
                        }
                    }
                }
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6 // You can enhance this validation based on requirements
    }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        binding.etUsername.text.clear()
        binding.etPassword.text.clear()
    }
}
