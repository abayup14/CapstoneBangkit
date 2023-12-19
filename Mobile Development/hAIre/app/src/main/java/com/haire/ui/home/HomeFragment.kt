package com.haire.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.haire.ListLowongansQuery
import com.haire.ProfileCompanyQuery
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.databinding.FragmentHomeBinding
import com.haire.ui.detail.DetailActivity
import com.haire.ui.openjobs.JobAdapter
import com.haire.util.showLoading
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel> { ViewModelFactory(requireContext()) }
    private var adapter: JobAdapter? = null

    private lateinit var sliderAdapter: SliderAdapter
    private val drawableList = listOf(
        R.drawable.ic_image1,
        R.drawable.ic_image2,
        R.drawable.ic_image3
    )
    private var currentPage = 0
    private val handler = Handler(Looper.getMainLooper())
    private val delay = 3000L

    private val updateSliderRunnable: Runnable = object : Runnable {
        override fun run() {
            if (currentPage == sliderAdapter.itemCount) {
                currentPage = 0
            }
            binding.viewPager.setCurrentItem(currentPage++, true)
            handler.postDelayed(this, delay)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        sliderAdapter = SliderAdapter(drawableList)
        binding.viewPager.adapter = sliderAdapter

        sliderAdapter.addIndicatorView(R.id.indicator1)
        sliderAdapter.addIndicatorView(R.id.indicator2)
        sliderAdapter.addIndicatorView(R.id.indicator3)

        handler.postDelayed(updateSliderRunnable, delay)

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it, binding.progressBar)
        }

        viewModel.getListLoker()
        viewModel.loker.observe(viewLifecycleOwner) { listJobs ->
            val requests = listJobs.map { job ->
                viewModel.getProfileCompanyAsync(job?.company_id!!)
            }

            lifecycleScope.launch {
                val companies = requests.awaitAll()
                setData(listJobs, companies)
            }
        }
        return binding.root
    }

    private fun setData(
        listJobs: List<ListLowongansQuery.Lowongan?>,
        companes: List<ProfileCompanyQuery.Company?>
    ) {
        binding.apply {
            adapter = JobAdapter(listJobs, companes) {
                val detailIntent = Intent(requireActivity(), DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_JOBS_ID, it)
                startActivity(detailIntent)
            }
            rvFeed.layoutManager = LinearLayoutManager(context)
            rvFeed.adapter = adapter
        }
    }

    override fun onDestroyView() {
        handler.removeCallbacks(updateSliderRunnable)
        super.onDestroyView()
        _binding = null
    }
}