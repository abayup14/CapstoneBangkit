package com.haire.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.haire.MainActivity
import com.haire.R
import com.haire.databinding.ActivityLoginBinding
import com.haire.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbUser: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString().replace(".", ",")
            val password = binding.passwordEditText.text.toString()
            dbUser = FirebaseDatabase.getInstance().getReference("users")

            if (email.isEmpty() || password.isEmpty()) {
                showText("Email or password still empty")
            } else {
                dbUser.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(email).exists()) {
                            if (snapshot.child(email).child("password").getValue(String::class.java)
                                    .equals(password)
                            ) {
                                AlertDialog.Builder(this@LoginActivity).apply {
                                    setMessage(getString(R.string.login_success))
                                    setTitle("Login")
                                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                                        startActivity(
                                            Intent(
                                                this@LoginActivity,
                                                MainActivity::class.java
                                            )
                                        )
                                        finish()
                                    }
                                    show()
                                }
                            } else {
                                showText("Password is not correct")
                            }
                        } else {
                            showText("Data belum terdaftar")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        showText(error.message)
                    }
                })
            }
        }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }

    private fun showText(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}