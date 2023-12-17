package com.haire.ui.company.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityDetailJobBinding
import com.haire.ui.company.CompanyViewModel

class DetailJobActivity : AppCompatActivity() {
    private var _binding: ActivityDetailJobBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CompanyViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val idLowongan = intent.getIntExtra(EXTRA_ID, 0)
        viewModel.getDetailLowongan(idLowongan)
        viewModel.detailLowongan.observe(this) {
//            Glide.with(this)
//                .load(it.)
            binding.tvPerusahaan.text = it?.nama
//            binding.tvPerusahaan.text = company.nama
//            binding.tvAlamat.text = company.address
            binding.tvDetail.text = it?.deskripsi
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}