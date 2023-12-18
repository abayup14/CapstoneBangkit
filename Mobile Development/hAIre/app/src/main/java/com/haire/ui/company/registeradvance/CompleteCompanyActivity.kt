package com.haire.ui.company.registeradvance

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityCompleteCompanyBinding
import com.haire.ui.company.CompanyViewModel
import com.haire.util.showText

class CompleteCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompleteCompanyBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<CompanyViewModel> { ViewModelFactory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnOpenFile.setOnClickListener {
            startGallery()
        }

        binding.btnUpload.setOnClickListener {
            val description = binding.edtDescStory.text.toString()
            if (description.isEmpty()) {
                showText(this, "Description still empty")
            } else {
                viewModel.getSession().observe(this) {
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
//                    viewModel.updateDatabaseCompany(
//                        email,
//                        description,
//                        imageUrl,
//                        onSuccess = {
//                            finish()
//                        },
//                        onFailure = { exception ->
//                            exception.printStackTrace()
//                        }
//                    )
                },
                onFailure = { exception ->
                    exception.printStackTrace()
                }
            )
        } ?: showText(this, getString(R.string.message_null_picture))
    }
}