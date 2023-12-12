package com.haire.ui.company.registeradvance

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityCompleteCompanyBinding
import com.haire.ui.company.CompanyActivity
import com.haire.ui.company.CompanyViewModel
import com.haire.util.Helper

class CompleteCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompleteCompanyBinding
    private var currentImageUri: Uri? = null
    private lateinit var helper: Helper
    private val viewModel by viewModels<CompanyViewModel>{ ViewModelFactory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        helper = Helper()

        viewModel.getSession().observe(this) {
            viewModel.getCompanyData(it.email)
        }

        viewModel.companyData.observe(this) {
            if (it.photoUrl != "") {
                startActivity(Intent(this@CompleteCompanyActivity, CompanyActivity::class.java))
                finish()
            }
        }

        binding.btnOpenFile.setOnClickListener {
            startGallery()
        }

        binding.btnUpload.setOnClickListener {
            val description = binding.edtDescStory.text.toString()
            if (description.isEmpty()) {
                helper.showText(this, "Description still empty")
            } else {
                viewModel.getSession().observe(this){
                    saveProfile(it.email, description)
                }
            }
        }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivAddStory.setImageURI(it)
        }
    }

    private fun saveProfile(email: String, description: String) {
        currentImageUri?.let { uri ->
            viewModel.saveProfile(
                uri,
                onSuccess = { imageUrl ->
                    viewModel.updateDatabaseCompany(
                        email,
                        description,
                        imageUrl,
                        onSuccess = {
                            finish()
                        },
                        onFailure = { exception ->
                            exception.printStackTrace()
                        }
                    )
                },
                onFailure = { exception ->
                    exception.printStackTrace()
                }
            )
        } ?: helper.showText(this, getString(R.string.message_null_picture))
    }
}