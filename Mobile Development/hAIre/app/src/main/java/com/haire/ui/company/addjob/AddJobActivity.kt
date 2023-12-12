package com.haire.ui.company.addjob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.data.Company
import com.haire.data.Jobs
import com.haire.databinding.ActivityAddJobBinding
import com.haire.ui.company.CompanyViewModel

class AddJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddJobBinding
    private val viewModel by viewModels<CompanyViewModel> { ViewModelFactory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) {
            viewModel.getCompanyData(it.email)
        }

        binding.btnAdd.setOnClickListener {
            val pekerjaan = binding.edtJob.text.toString()
            val deskripsi = binding.edtDeskripsi.text.toString()
            val alamat = binding.edtAddress.text.toString()
            val jmlButuh = binding.edtButuh.text.toString().toInt()

            viewModel.companyData.observe(this) {
                val job = Jobs(it, pekerjaan, deskripsi, jmlButuh)
                viewModel.addJob(job)
            }
            finish()
        }
    }
}