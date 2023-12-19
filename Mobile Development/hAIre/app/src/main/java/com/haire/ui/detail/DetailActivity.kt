package com.haire.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.haire.GetLowonganQuery
import com.haire.ListSkillRequiredQuery
import com.haire.ProfileCompanyQuery
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityDetailBinding
import com.haire.util.showLoading

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> { ViewModelFactory(this) }
    private var isHandled: Boolean = false
    private var disableBtn: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.btnBack.setOnClickListener { finish() }

        viewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBar)
        }

        val jobId = intent.getIntExtra(EXTRA_JOBS_ID, 0)
        viewModel.getSession().observe(this) {
            viewModel.getUserProfile(it.id)
            viewModel.getJaccardSkill(it.id, jobId)
            viewModel.getListApplied(it.id)
        }

        viewModel.listLoker.observe(this) { listLoker ->
            disableBtn = listLoker.any { job -> job?.id == jobId }
            binding.btnApply.visibility = if (disableBtn) View.GONE else View.VISIBLE
        }

        viewModel.getSkillRequired(jobId)
        viewModel.getDetail(jobId)
        viewModel.detailLowongan.observe(this) { lowongan ->
            viewModel.getCompany(lowongan?.company_id!!)
            viewModel.companyData.observe(this) {
                setData(lowongan, it)
            }
        }


        viewModel.skillRequired.observe(this) {
            if (!isHandled) {
                setSkillData(it)
                isHandled = true
            }
        }

        binding.btnApply.setOnClickListener {
            viewModel.apply()
        }

        viewModel.success.observe(this) {
            if (it) {
                finish()
            }
        }
    }

    private fun setData(detail: GetLowonganQuery.Lowongan?, company: ProfileCompanyQuery.Company?) {
        binding.apply {
//            Glide.with(this@DetailActivity)
//                .load(company?.url_photo)
//                .into(ivJobs)
            tvPekerjaan.text = detail?.nama.toString()
            tvAlamat.text = company?.alamat
            tvDetail.text = detail?.deskripsi
            tvDetail2.text = company?.deskripsi
            tvPerusahaan.text = company?.nama
        }
    }

    private fun setSkillData(listSkill: List<ListSkillRequiredQuery.Skill?>) {
        for (skill in listSkill) {
            val newChip = Chip(this)
            newChip.text = skill?.nama

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.marginEnd = 8
            newChip.layoutParams = layoutParams

            binding.linearLayout.addView(newChip)
        }
    }

    companion object {
        const val EXTRA_JOBS_ID = "extra_jobs_id"
    }
}