package com.haire.ui.status

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.haire.ListApplyUserQuery
import com.haire.ListLowonganUserApplyQuery
import com.haire.ProfileCompanyQuery
import com.haire.ViewModelFactory
import com.haire.databinding.FragmentStatusBinding
import com.haire.ui.detail.DetailActivity
import com.haire.util.showLoading
import kotlinx.coroutines.launch

class StatusFragment : Fragment() {
    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!

    private var adapter: StatusAdapter? = null
    private val viewModel by viewModels<StatusViewModel> { ViewModelFactory(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it, binding.progressBar)
        }
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            viewModel.getListLokerStatus(user.id)
            viewModel.listLoker.observe(viewLifecycleOwner) { listStatus ->
                viewModel.getApplyStatusAsync(user.id)
                viewModel.listApplyUser.observe(viewLifecycleOwner) {
                    lifecycleScope.launch {
                        val companiesDeferred = listStatus.map { viewModel.getProfileCompanyAsync(it?.company_id!!) }
                        val companies = companiesDeferred.map { it.await() }

                        setData(it, listStatus, companies)
                    }
                }

            }
        }
        return binding.root
    }

    private fun setData(
        listApply: List<ListApplyUserQuery.Apply?>,
        listStatus: List<ListLowonganUserApplyQuery.Lowongan?>,
        companies: List<ProfileCompanyQuery.Company?>
    ) {

        binding.apply {
            adapter = StatusAdapter(listApply, listStatus, companies) {
                val detailIntent = Intent(requireActivity(), DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_JOBS_ID, it)
                startActivity(detailIntent)
            }
            rvStatus.layoutManager = LinearLayoutManager(context)
            rvStatus.adapter = adapter
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