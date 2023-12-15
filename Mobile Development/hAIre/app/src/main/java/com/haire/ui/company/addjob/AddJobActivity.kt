package com.haire.ui.company.addjob

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityAddJobBinding
import com.haire.ui.company.CompanyViewModel
import com.haire.util.showText

class AddJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddJobBinding
    private val viewModel by viewModels<CompanyViewModel> { ViewModelFactory(this) }
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
                showText(this, "All field must be filled")
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