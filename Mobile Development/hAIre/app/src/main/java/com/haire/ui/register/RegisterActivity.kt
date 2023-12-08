package com.haire.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.haire.R
import com.haire.databinding.ActivityRegisterBinding
import com.haire.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbUser: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        dbUser = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://haire-test-de01d-default-rtdb.firebaseio.com/")

        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString().replace(".", ",")
            val pass = binding.edtPassword.text.toString()
            val rePass = binding.edtRePassword.text.toString()
            val phone = binding.edtPhone.text.toString()
            val homelessIsChecked = binding.homelessRg.checkedRadioButtonId
            val disabledIsChecked = binding.disabledRg.checkedRadioButtonId
            var homeless = false
            var disabled = false

            binding.homelessRg.setOnCheckedChangeListener { _, checkedId ->
                val selectedRadioButton: RadioButton = findViewById(checkedId)
                when (selectedRadioButton.text.toString()) {
                    "yes" -> homeless = false
                    "no" -> homeless = true
                }
            }

            binding.disabledRg.setOnCheckedChangeListener { _, checkedId ->
                val selectedRadioButton: RadioButton = findViewById(checkedId)
                when (selectedRadioButton.text.toString()) {
                    "yes" -> disabled = true
                    "no" -> disabled = false
                }
            }

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || rePass.isEmpty() || phone.isEmpty() || homelessIsChecked == -1 || disabledIsChecked == -1) {
                Toast.makeText(
                    this@RegisterActivity,
                    "There's still empty field",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (pass != rePass) {
                Toast.makeText(this@RegisterActivity, "Password didn't match", Toast.LENGTH_SHORT)
                    .show()
            } else {
                dbUser = FirebaseDatabase.getInstance().getReference("users")
                dbUser.child(email).child("name").setValue(name)
                dbUser.child(email).child("email").setValue(email)
                dbUser.child(email).child("phone").setValue(phone)
                dbUser.child(email).child("password").setValue(pass)
                dbUser.child(email).child("isHomeless").setValue(homeless)
                dbUser.child(email).child("isDisabled").setValue(disabled)

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
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }
}