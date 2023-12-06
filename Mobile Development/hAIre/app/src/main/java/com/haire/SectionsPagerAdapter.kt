package com.haire

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.haire.ui.openjobs.OpenJobsFragment
import com.haire.ui.status.StatusFragment

class SectionsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = OpenJobsFragment()
            1 -> fragment = StatusFragment()
        }
        return fragment as Fragment
    }

    fun getFragment(position: Int): Fragment? {
        return when (position) {
            0 -> OpenJobsFragment()
            1 -> StatusFragment()
            else -> null
        }
    }

}