package com.haire.ui.company.detail

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.haire.ListApplyLowonganQuery
import com.haire.ListSkillRequiredQuery
import com.haire.ProfileUserQuery
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityDetailJobBinding
import com.haire.util.showLoading
import com.haire.util.showText
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class DetailJobActivity : AppCompatActivity() {
    private var _binding: ActivityDetailJobBinding? = null
    private val binding get() = _binding!!
    private var adapter: UserAdapter? = null
    var isHandled: Boolean = false
    private val viewModel by viewModels<DetailJobViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val id = intent.getIntExtra(EXTRA_ID, 0)
        viewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBar)
        }

        viewModel.toastMsg.observe(this) {
            showText(this, it)
        }

        binding.btnBack.setOnClickListener { finish() }

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
            val requests = it.map { user ->
                viewModel.getProfileCompanyAsync(user?.user_iduser!!)
            }
            lifecycleScope.launch {
                val user = requests.awaitAll()
                setLeaderBoardData(it, user)
            }
        }

        viewModel.getSkills(id)
        viewModel.skill.observe(this) {
            if (!isHandled) {
                setSkillData(it)
                isHandled = true
            }
        }
    }

    private fun setLeaderBoardData(
        listApply: List<ListApplyLowonganQuery.Apply?>,
        listUser: List<ProfileUserQuery.User?>
    ) {
        val filteredList = listApply.filter { it?.status !in listOf("Diterima", "Ditolak") }
        adapter = UserAdapter(filteredList, listUser, this::onAcceptClick, this::onRejectClick)
        binding.rvLeaderboard.layoutManager = LinearLayoutManager(this)
        binding.rvLeaderboard.adapter = adapter
    }

    private fun onAcceptClick(idUser: Int) {
        val idLowongan = intent.getIntExtra(EXTRA_ID, 0)
        viewModel.updateUserApplyStatus(idUser, idLowongan, "Diterima")
        showText(this, "Accepted")
        val updatedListApply = adapter?.getListApply()?.toMutableList()
        updatedListApply?.removeAll { it?.user_iduser == idUser }
        adapter?.updateListApply(updatedListApply!!)
    }

    private fun onRejectClick(idUser: Int) {
        val id = intent.getIntExtra(EXTRA_ID, 0)
        viewModel.updateUserApplyStatus(idUser, id, "Ditolak")
        showText(this, "Rejected")
        val updatedListApply = adapter?.getListApply()?.toMutableList()
        updatedListApply?.removeAll { it?.user_iduser == idUser }
        adapter?.updateListApply(updatedListApply!!)
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