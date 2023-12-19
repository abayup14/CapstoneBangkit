package com.haire.ui.profile.company

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityCompanyProfileBinding
import com.haire.ui.company.registeradvance.CompleteCompanyActivity
import com.haire.util.showLoading

class CompanyProfileActivity : AppCompatActivity() {
    private var _binding: ActivityCompanyProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CompanyProfileViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCompanyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBar)
        }

        binding.btnEdtPhoto.setOnClickListener {
            startActivity(Intent(this@CompanyProfileActivity, CompleteCompanyActivity::class.java))
        }

        viewModel.getUser().observe(this) {
            if (it.isCompany) {
                viewModel.getProfileUI(it.id)
                viewModel.profileCompanyData.observe(this) { company ->
                    if (company.url_photo != "") {
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