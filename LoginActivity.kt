package com.example.researchmanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.researchmanager.databinding.ActivityLoginBinding
import com.example.researchmanager.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            btnLogin.setOnClickListener {
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                if (username.isBlank() || password.isBlank()) {
                    Toast.makeText(this@LoginActivity, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Show loading indicator while logging in
                progressBar.visibility = View.VISIBLE

                lifecycleScope.launch {
                    try {
                        val user = userViewModel.login(username, password)
                        progressBar.visibility = View.GONE

                        if (user != null) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("user_id", user.id)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }
}
