package com.haire.ui.company.addjob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.data.Company
import com.haire.data.Jobs
import com.haire.databinding.ActivityAddJobBinding
import com.haire.ui.company.CompanyViewModel
import com.haire.util.Helper

class AddJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddJobBinding
    private val viewModel by viewModels<CompanyViewModel> { ViewModelFactory(this) }
    private val helper = Helper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.getSession().observe(this) {
            viewModel.getCompanyData(it.email)
        }

        binding.btnAdd.setOnClickListener {
            val pekerjaan = binding.edtJob.text.toString()
            val deskripsi = binding.edtDeskripsi.text.toString()
            val jmlButuh = binding.edtButuh.text.toString()

            if (pekerjaan.isEmpty() || deskripsi.isEmpty() || jmlButuh.isEmpty()) {
                helper.showText(this, "All field must be filled")
            } else {
                val butuhInt = jmlButuh.toInt()

                viewModel.companyData.observe(this) {
//                val job = Jobs(it, pekerjaan, deskripsi, jmlButuh)
//                viewModel.addJob(job)
                }
                finish()
            }
        }
    }
}