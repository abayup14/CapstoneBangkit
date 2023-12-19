package com.haire.ui.company.detail

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.haire.ListApplyLowonganQuery
import com.haire.ListSkillRequiredQuery
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityDetailJobBinding
import com.haire.util.showLoading

class DetailJobActivity : AppCompatActivity() {
    private var _binding: ActivityDetailJobBinding? = null
    private val binding get() = _binding!!
    private var adapter: UserAdapter? = null
    private val viewModel by viewModels<DetailJobViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBar)
        }

        binding.btnBack.setOnClickListener { finish() }

        val id = intent.getIntExtra(EXTRA_ID, 0)

        viewModel.getListApplyLoker(id)
        viewModel.getLowongan(id)
        viewModel.detailLowongan.observe(this) { job ->
            binding.apply {
                tvPekerjaan.text = job?.nama
                tvJmlh.text = job?.jmlh_butuh.toString()
                tvDetail.text = job?.deskripsi
            }
        }

        viewModel.listLeaderBoard.observe(this) {
            setLeaderBoardData(it)
        }

        viewModel.getSkills(id)
        viewModel.skill.observe(this) {
                setSkillData(it)
        }
    }

    private fun setLeaderBoardData(listApply: List<ListApplyLowonganQuery.Apply?>) {
        adapter = UserAdapter(listApply) {
            finish()
        }
        binding.rvLeaderboard.layoutManager = LinearLayoutManager(this)
        binding.rvLeaderboard.adapter = adapter
    }

    private fun setSkillData(listSkill: List<ListSkillRequiredQuery.Skill?>) {
        for (skill in listSkill) {
            val newChip = Chip(this)
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

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}