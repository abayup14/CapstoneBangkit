package com.haire.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.haire.MainActivity
import com.haire.R
import com.haire.databinding.ActivityLoginBinding
import com.haire.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.login_success))
                setTitle("Login")
                setPositiveButton(getString(R.string.yes)) { _, _ ->
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                show()
            }
        }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }
}