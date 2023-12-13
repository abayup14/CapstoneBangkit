package com.haire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.haire.databinding.OpenJobsFragmentBinding
import com.haire.model.InitialJobsValue
import com.haire.model.Jobs

class OpenJobsFragment : Fragment() {

    private lateinit var binding: OpenJobsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OpenJobsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData(InitialJobsValue.dummyJobs)
    }

    private fun setData(listJobs: List<Jobs>) {
        val listJob = ArrayList<Jobs>()
        binding.apply {
            for (a in listJobs) {
                listJob.clear()
                listJob.addAll(listJobs)
            }
            val adapter = JobAdapter(listJobs)
            rvOpenJobs.layoutManager = LinearLayoutManager(context)
            rvOpenJobs.adapter = adapter
        }
    }
}