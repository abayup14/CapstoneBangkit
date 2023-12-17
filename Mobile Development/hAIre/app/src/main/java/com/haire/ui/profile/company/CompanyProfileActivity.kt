package com.haire.ui.profile.company

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityCompanyProfileBinding

class CompanyProfileActivity : AppCompatActivity() {
    private var _binding: ActivityCompanyProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CompanyProfileViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCompanyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.getUser().observe(this) {
            if (it.isCompany) {
                viewModel.getProfileUI(it.id)
                viewModel.profileCompanyData.observe(this) { company ->
                    if (company.url_photo != ""){
                        Glide.with(this)
                            .load(company.url_photo)
                            .circleCrop()
                            .into(binding.profileImage)
                    }
                    binding.apply {
                        nameText.text = company.nama
                        addressText.text = company.alamat
                        descriptionText.text = company.deskripsi
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}