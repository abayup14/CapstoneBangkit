package com.haire.ui.profile.setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.databinding.ActivitySettingProfileBinding
import com.haire.ui.profile.ProfileViewModel
import com.haire.ui.profile.editprofile.EditProfileActivity
import com.haire.ui.welcome.WelcomeActivity

class SettingProfileActivity : AppCompatActivity() {
    private var _binding: ActivitySettingProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.btnAbout.setOnClickListener {
//            startActivity(Intent(this@SettingProfileActivity, AboutActivity::class.java))
        }
//        binding.btnDelete.setOnClickListener {
//            showDialog()
//        }
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(this@SettingProfileActivity, WelcomeActivity::class.java))
            finish()
        }
        binding.btnEdit.setOnClickListener {
            startActivity(Intent(this@SettingProfileActivity, EditProfileActivity::class.java))
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.delete_confirmation))
            setTitle("Delete Account")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes") { _, _ ->
//                viewModel.getUser().observe(this@SettingProfileActivity) {
//                    viewModel.deleteAccount(it.email)
//                }
                viewModel.logout()
                startActivity(
                    Intent(
                        this@SettingProfileActivity,
                        WelcomeActivity::class.java
                    )
                )
                finish()
            }
            show()
        }
    }
}