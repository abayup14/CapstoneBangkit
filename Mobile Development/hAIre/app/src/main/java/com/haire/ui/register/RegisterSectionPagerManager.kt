package com.haire.ui.register

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.haire.ui.register.company.CompanyFragment
import com.haire.ui.register.jobseeker.JobSeekerFragment

class RegisterSectionPagerManager(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = JobSeekerFragment()
            1 -> fragment = CompanyFragment()
        }
        return fragment as Fragment
    }
}