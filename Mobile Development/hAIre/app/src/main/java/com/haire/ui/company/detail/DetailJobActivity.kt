package com.haire.ui.company.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.haire.ListSkillRequiredQuery
import com.haire.ListUserSkillsQuery
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityDetailJobBinding
import com.haire.ui.profile.ProfileViewModel

class DetailJobActivity : AppCompatActivity() {
    private var _binding: ActivityDetailJobBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailJobViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailJobBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnBack.setOnClickListener { finish() }

        viewModel.getLowongan(intent.getIntExtra(EXTRA_ID, 0))
        viewModel.detailLowongan.observe(this) {job ->
            binding.apply {
                tvPekerjaan.text = job?.nama
                tvJmlh.text = job?.jmlh_butuh.toString()
                tvDetail.text = job?.deskripsi
            }
        }

        viewModel.getSkills(intent.getIntExtra(EXTRA_ID, 0))
        viewModel.skill.observe(this) {
            setSkillData(it)
        }
    }

    private fun setSkillData(listSkill: List<ListSkillRequiredQuery.Skill?>) {
        for (skill in listSkill) {
            val newChip = Chip(this)
            newChip.text = skill?.nama

            // Atur parameter layout untuk Chip
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.marginEnd = 8
            newChip.layoutParams = layoutParams

            binding.linearLayout.addView(newChip)
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}