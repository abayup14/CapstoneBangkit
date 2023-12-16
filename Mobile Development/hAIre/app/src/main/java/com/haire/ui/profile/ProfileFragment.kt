package com.haire.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.haire.ViewModelFactory
import com.haire.databinding.FragmentProfileBinding
import com.haire.ui.profile.education.AddEducationActivity
import com.haire.ui.profile.experience.AddExperienceActivity
import com.haire.ui.profile.setting.SettingProfileActivity
import com.haire.ui.profile.skill.AddSkillActivity
import com.haire.ui.welcome.WelcomeActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel> { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser().observe(viewLifecycleOwner) { it ->
            if (it.isLogin) {
                viewModel.getProfileUI(it.email)
//                viewModel.profileAccount.observe(viewLifecycleOwner) {user ->
//                    if (user.photoUrl != ""){
//                        Glide.with(requireActivity())
//                            .load(user.photoUrl)
//                            .circleCrop()
//                            .into(binding.profilePicture)
//                    }
//                    binding.apply {
//                        aboutMe.text = user.about
//                        username.text = user.name
//                    }
//                }
                binding.btnSetting.setOnClickListener {
                    startActivity(Intent(requireActivity(), SettingProfileActivity::class.java))
                }
            }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}