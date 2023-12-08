package com.haire.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.haire.MainActivity
import com.haire.R
import com.haire.databinding.ActivityRegisterBinding
import com.haire.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnRegister.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.register_success))
                setTitle("Register")
                setPositiveButton(getString(R.string.login)) { _, _ ->
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }
                show()
            }
        }
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }
}