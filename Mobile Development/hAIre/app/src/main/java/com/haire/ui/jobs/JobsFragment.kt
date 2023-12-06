package com.haire.ui.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.haire.R
import com.haire.SectionsPagerAdapter
import com.haire.databinding.FragmentJobsBinding
import com.haire.ui.openjobs.OpenJobsFragment
import com.haire.ui.status.StatusFragment

class JobsFragment : Fragment() {
    private var _binding: FragmentJobsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobsBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity())
        binding.apply {
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val selectedPosition = viewPager.currentItem
                    sectionsPagerAdapter.let {
                        when (selectedPosition) {
                            0 -> {
                                val openJobsFragment =
                                    it.getFragment(selectedPosition) as OpenJobsFragment
                                openJobsFragment.getAdapter()?.filter?.filter(newText)
                            }

                            1 -> {
                                val statusFragment =
                                    it.getFragment(selectedPosition) as StatusFragment
                                statusFragment.getAdapter()?.filter?.filter(newText)
                            }

                            else -> {}
                        }
                    }
                    return true
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}