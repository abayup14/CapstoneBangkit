package com.haire.ui.register

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Patterns
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.haire.R
import com.haire.SectionsPagerAdapter
import com.haire.ViewModelFactory
import com.haire.data.User
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

        val sectionsPagerAdapter = RegisterSectionPagerManager(this)
        binding.apply {
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_register_1,
            R.string.tab_register_2
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

//class RegisterActivity : AppCompatActivity() {
//    private var _binding: ActivityRegisterBinding? = null
//    private val binding get() = _binding!!
//    private val viewModel by viewModels<RegisterViewModel> { ViewModelFactory(this) }
//    private var homeless = false
//    private var disabled = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        _binding = ActivityRegisterBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        supportActionBar?.hide()
//
//        binding.homelessRg.setOnCheckedChangeListener { _, checkedId ->
//            val selectedRadioButton: RadioButton = findViewById(checkedId)
//            when (selectedRadioButton.text.toString()) {
//                "Yes" -> homeless = false
//                "No" -> homeless = true
//            }
//        }
//
//        binding.disabledRg.setOnCheckedChangeListener { _, checkedId ->
//            val selectedRadioButton: RadioButton = findViewById(checkedId)
//            when (selectedRadioButton.text.toString()) {
//                "Yes" -> disabled = true
//                "No" -> disabled = false
//            }
//        }
//
//        viewModel.isEmailExist.observe(this) { emailExist ->
//            if (emailExist) {
//                showText("Email already exists")
//            } else {
//                showAlert()
//            }
//        }
//
//        val validation = AwesomeValidation(ValidationStyle.BASIC)
//        validation.apply {
//            addValidation(binding.edtEmail,
//                Patterns.EMAIL_ADDRESS,
//                getString(R.string.invalid_email))
//            addValidation(binding.edtPassword, ".{6,}", getString(R.string.invalid_password))
//            addValidation(binding.edtRePassword,
//                binding.edtPassword,
//                getString(R.string.password_not_match))
//        }
//
//        binding.btnRegister.setOnClickListener {
//            val name = binding.edtName.text.toString()
//            val email = binding.edtEmail.text.toString().replace(".", ",")
//            val pass = binding.edtPassword.text.toString()
//            val rePass = binding.edtRePassword.text.toString()
//            val phone = binding.edtPhone.text.toString()
//            val homelessIsChecked = binding.homelessRg.checkedRadioButtonId
//            val disabledIsChecked = binding.disabledRg.checkedRadioButtonId
//
//            val user = User(name, phone, email, homeless, disabled)
//
//            if (validation.validate()) {
//                if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || rePass.isEmpty() || phone.isEmpty() || homelessIsChecked == -1 || disabledIsChecked == -1) {
//                    showText(getString(R.string.empty_field))
//                } else {
//                    viewModel.registerAccount(user, pass)
//                }
//            }
//        }
//
//        binding.tvLogin.setOnClickListener {
//            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
//            finish()
//        }
//    }
//
//    private fun showText(message: String) {
//        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
//    }
//
//    private fun showAlert() {
//        AlertDialog.Builder(this).apply {
//            setMessage(getString(R.string.register_success))
//            setTitle("Register")
//            setPositiveButton(getString(R.string.login)) { _, _ ->
//                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
//                finish()
//            }
//            show()
//        }
//    }
//}