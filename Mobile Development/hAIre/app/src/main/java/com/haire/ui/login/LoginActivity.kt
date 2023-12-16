package com.haire.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.haire.MainActivity
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.data.UserModel
import com.haire.databinding.ActivityLoginBinding
import com.haire.ui.company.CompanyActivity
import com.haire.ui.company.registeradvance.CompleteCompanyActivity
import com.haire.ui.register.RegisterActivity
import com.haire.util.showText

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.toastMsg.observe(this) { msg ->
            showText(this, msg)
        }

        val validation = AwesomeValidation(ValidationStyle.BASIC)
        validation.apply {
            addValidation(
                binding.emailEditText,
                Patterns.EMAIL_ADDRESS,
                getString(R.string.invalid_email)
            )
            addValidation(binding.passwordEditText, ".{6,}", getString(R.string.invalid_password))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (validation.validate()) {
                if (email.isEmpty() || password.isEmpty()) {
                    showText(this, getString(R.string.email_password_empty))
                } else {
                    viewModel.loginAcc(email, password)
                    viewModel.success.observe(this) {
                        if (it) {
                            viewModel.id.observe(this) { id ->
                                viewModel.saveUser(UserModel(id, email, true, isCompany = false))
                            }
                            showDialog(it, MainActivity::class.java)
                        }
                    }
                    viewModel.isCompany.observe(this) {
                        if (it) {
                            viewModel.id.observe(this) { id ->
                                viewModel.saveUser(UserModel(id, email, true, isCompany = true))
                            }
                            showDialog(it, CompanyActivity::class.java)
                        }
                    }
                }
            }
        }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }

    private fun showDialog(isSuccess: Boolean, page: Class<*>) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.login_success))
                setTitle("Login")
                setPositiveButton(getString(R.string.yes)) { _, _ ->
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            page
                        )
                    )
                    finish()
                }
                show()
            }
        }
    }
}