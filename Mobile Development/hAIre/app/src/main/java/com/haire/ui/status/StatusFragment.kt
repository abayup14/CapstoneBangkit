package com.haire.ui.status

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.haire.R
import com.haire.data.InitialDummyValue
import com.haire.data.Status
import com.haire.databinding.FragmentStatusBinding
import com.haire.ui.detail.DetailActivity
import com.haire.ui.openjobs.OpenJobsViewModel

class StatusFragment : Fragment() {
    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!

    private var adapter: StatusAdapter? = null
    private val viewModel by viewModels<StatusViewModel> { ViewModelProvider.NewInstanceFactory() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.status.observe(viewLifecycleOwner) {
            setData(it)
        }
    }

    private fun setData(listStatus: List<Status>) {
        binding.apply {
            adapter = StatusAdapter(listStatus) {
                val detailIntent = Intent(requireActivity(), DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_JOBS, it)
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