package com.haire.ui.company

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.haire.ListLowonganCompanyQuery
import com.haire.ProfileCompanyQuery
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityCompanyBinding
import com.haire.ui.company.addjob.AddJobActivity
import com.haire.ui.company.detail.DetailJobActivity
import com.haire.ui.profile.company.CompanyProfileActivity
import com.haire.ui.welcome.WelcomeActivity
import com.haire.util.showLoading
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class CompanyActivity : AppCompatActivity() {
    private var _binding: ActivityCompanyBinding? = null
    private val binding get() = _binding!!
    private var adapter: JobCompanyAdapter? = null
    private val viewModel by viewModels<CompanyViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBar)
        }

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, CompanyProfileActivity::class.java))
        }
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(this@CompanyActivity, WelcomeActivity::class.java))
            finish()
        }
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this@CompanyActivity, AddJobActivity::class.java))
        }

        viewModel.getSession().observe(this) { userModel ->
            viewModel.getLokerCompany(userModel.id)
            viewModel.lokerCompany.observe(this) { listJobs ->
                val requests = listJobs.map { job ->
                    viewModel.getProfileCompanyAsync(job?.company_id!!)
                }

                lifecycleScope.launch {
                    val companies = requests.awaitAll()
                    setData(listJobs, companies)
                }
            }
        }
    }

    private fun setData(
        listJobs: List<ListLowonganCompanyQuery.Lowongan?>,
        companies: List<ProfileCompanyQuery.Company?>
    ) {
        binding.apply {
            adapter = JobCompanyAdapter(listJobs, companies) {
                val intentToDetail = Intent(this@CompanyActivity, DetailJobActivity::class.java)
                intentToDetail.putExtra(DetailJobActivity.EXTRA_ID, it)
                startActivity(intentToDetail)
            }
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