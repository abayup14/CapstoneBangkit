package com.haire.ui.company

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.data.Jobs
import com.haire.databinding.ActivityCompanyBinding
import com.haire.ui.detail.DetailActivity
import com.haire.ui.company.addjob.AddJobActivity
import com.haire.ui.company.registeradvance.CompleteCompanyActivity
import com.haire.ui.login.LoginActivity
import com.haire.ui.openjobs.JobAdapter
import com.haire.ui.openjobs.OpenJobsViewModel
import com.haire.ui.welcome.WelcomeActivity
import com.haire.util.showText

class CompanyActivity : AppCompatActivity() {
    private var _binding: ActivityCompanyBinding? = null
    private val binding get() = _binding!!
    private var adapter: JobAdapter? = null
    private val viewModel by viewModels<CompanyViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(this@CompanyActivity, WelcomeActivity::class.java))
            finish()
        }
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this@CompanyActivity, AddJobActivity::class.java))
        }
    }

    private fun setData(listJobs: List<Jobs>) {
        binding.apply {
//            adapter = JobAdapter(listJobs) {
//                val detailIntent = Intent(this@CompanyActivity, DetailActivity::class.java)
//                detailIntent.putExtra(DetailActivity.EXTRA_JOBS, it)
//                startActivity(detailIntent)
//            }
            rvOpenVacancy.layoutManager = LinearLayoutManager(applicationContext)
            rvOpenVacancy.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        binding.searchView.apply {
            setQuery("", false)
            clearFocus()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter?.filter?.filter(newText)
                    return true
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}