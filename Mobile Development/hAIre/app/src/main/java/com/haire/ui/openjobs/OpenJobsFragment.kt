package com.haire.ui.openjobs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.haire.ListLowongansQuery
import com.haire.ProfileCompanyQuery
import com.haire.ViewModelFactory
import com.haire.databinding.FragmentOpenJobsBinding
import com.haire.ui.detail.DetailActivity
import com.haire.util.showLoading

class OpenJobsFragment : Fragment() {
    private var _binding: FragmentOpenJobsBinding? = null
    private val binding get() = _binding!!
    private var adapter: JobAdapter? = null
    private val viewModel by viewModels<OpenJobsViewModel> { ViewModelFactory(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenJobsBinding.inflate(inflater, container, false)
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it, binding.progressBar)
        }
        viewModel.getListLoker()
        viewModel.loker.observe(viewLifecycleOwner) {
            for (a in it) {
                viewModel.getProfileCompany(a?.company_id!!)
                Log.e("asw", "${a.company_id}")
            }
            viewModel.profileCompany.observe(viewLifecycleOwner) { company ->
                setData(it, company!!)
            }
        }
        return binding.root
    }

    private fun setData(
        listJobs: List<ListLowongansQuery.Lowongan?>,
        company: ProfileCompanyQuery.Company
    ) {
        binding.apply {
            adapter = JobAdapter(listJobs, company) {
                val detailIntent = Intent(requireActivity(), DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_JOBS_ID, it)
                startActivity(detailIntent)
            }
            rvOpenJobs.layoutManager = LinearLayoutManager(context)
            rvOpenJobs.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        binding.search.apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}