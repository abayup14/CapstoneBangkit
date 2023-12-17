package com.haire.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.haire.GetLowonganQuery
import com.haire.ProfileCompanyQuery
import com.haire.ViewModelFactory
import com.haire.data.Jobs
import com.haire.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> { ViewModelFactory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.btnBack.setOnClickListener { finish() }

        val jobId = intent.getIntExtra(EXTRA_JOBS_ID, 0)
        viewModel.getDetail(jobId)
        viewModel.detailLowongan.observe(this) { lowongan ->
            viewModel.getCompany(lowongan?.company_id!!)
            viewModel.companyData.observe(this) {
                setData(lowongan, it)
            }
        }
    }

    private fun setData(detail: GetLowonganQuery.Lowongan?, company: ProfileCompanyQuery.Company?) {
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(company?.url_photo)
                .into(ivJobs)
            tvPekerjaan.text = detail?.nama.toString()
            tvAlamat.text = company?.alamat
            btnApply.setOnClickListener {
                // apply
            }
        }
    }

    companion object {
        const val EXTRA_JOBS_ID = "extra_jobs_id"
    }
}