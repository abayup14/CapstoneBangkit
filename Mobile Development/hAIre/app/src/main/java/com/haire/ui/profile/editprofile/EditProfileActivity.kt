package com.haire.ui.profile.editprofile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityEditProfileBinding
import com.haire.ui.profile.ProfileViewModel
import com.haire.util.Helper

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var currentImageUri: Uri? = null
    private lateinit var helper: Helper
    private val viewModel by viewModels<ProfileViewModel> { ViewModelFactory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        helper = Helper()

        binding.btnOpenFile.setOnClickListener {
            startGallery()
        }

        binding.btnUpload.setOnClickListener {
            val description = binding.edtDescStory.text.toString()
            val age = binding.edtAge.text.toString()
            if (description.isEmpty() || age.isEmpty()) {
                helper.showText(this, "Description and age are still empty")
            } else {
                val ageInt = age.toInt()
                viewModel.getUser().observe(this) {
                    saveProfile(it.email, description, ageInt)
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

    private fun saveProfile(email: String, description: String, age: Int) {
        currentImageUri?.let { uri ->
            viewModel.saveProfile(
                uri,
                onSuccess = { imageUrl ->
                    viewModel.updateDatabase(
                        email,
                        description,
                        age,
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