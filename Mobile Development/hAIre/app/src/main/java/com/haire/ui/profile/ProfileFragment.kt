package com.haire.ui.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.haire.ListEdukasiQuery
import com.haire.ListPengalamanQuery
import com.haire.ListUserSkillsQuery
import com.haire.ViewModelFactory
import com.haire.databinding.FragmentProfileBinding
import com.haire.ui.profile.education.AddEducationActivity
import com.haire.ui.profile.education.EducationAdapter
import com.haire.ui.profile.experience.AddExperienceActivity
import com.haire.ui.profile.experience.ExperienceAdapter
import com.haire.ui.profile.setting.SettingProfileActivity
import com.haire.ui.profile.skill.AddSkillActivity
import com.haire.util.showLoading

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var adapter: ExperienceAdapter? = null
    private val viewModel by viewModels<ProfileViewModel> { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it, binding.progressBar)
        }
        viewModel.getUser().observe(viewLifecycleOwner) {
            if (it.isLogin) {
                viewModel.getProfileUI(it.id)
                viewModel.profileData.observe(viewLifecycleOwner) { user ->
                    if (user.url_photo != "") {
                        Glide.with(requireActivity())
                            .load(user.url_photo)
                            .circleCrop()
                            .into(binding.profilePicture)
                    }
                    binding.apply {
                        aboutMe.text = user.deskripsi
                        username.text = user.nama
                    }
                }
                viewModel.getPengalaman(it.id)
                viewModel.getEdukasi(it.id)
                viewModel.getSkills(it.id)
            }
        }

        viewModel.edu.observe(viewLifecycleOwner) {
            setEducationData(it)
        }

        viewModel.exp.observe(viewLifecycleOwner) {
            setExperienceData(it)
        }

        viewModel.skill.observe(viewLifecycleOwner) {
            setSkillData(it)
        }

        binding.btnSetting.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingProfileActivity::class.java))
        }
        binding.btnAddEducation.setOnClickListener {
            startActivity(Intent(requireActivity(), AddEducationActivity::class.java))
        }
        binding.btnAddExperience.setOnClickListener {
            startActivity(Intent(requireActivity(), AddExperienceActivity::class.java))
        }
        binding.btnAddSkill.setOnClickListener {
            startActivity(Intent(requireActivity(), AddSkillActivity::class.java))
        }
        return binding.root
    }

    private fun setEducationData(listEdu: List<ListEdukasiQuery.Edukasi?>) {
        val adapter = EducationAdapter(listEdu)
        binding.educationRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.educationRecyclerView.adapter = adapter
    }

    private fun setExperienceData(listExp: List<ListPengalamanQuery.Pengalaman?>) {
        adapter = ExperienceAdapter(listExp)
        binding.experienceRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.experienceRecyclerView.adapter = adapter
    }

    private fun setSkillData(listSkill: List<ListUserSkillsQuery.Skill?>) {
        binding.linearLayout.removeAllViews()
        for (skill in listSkill) {
            val newChip = Chip(context)
            newChip.text = skill?.nama

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.marginEnd = 8
            newChip.layoutParams = layoutParams

            binding.linearLayout.addView(newChip)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}