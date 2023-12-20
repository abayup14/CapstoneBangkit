package com.haire.ui.company.detail

import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.haire.ListApplyLowonganQuery
import com.haire.ListSkillRequiredQuery
import com.haire.ProfileUserQuery
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityDetailJobBinding
import com.haire.util.showLoading
import com.haire.util.showText
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
            viewModel.getProfileCompany(job?.company_id!!)
            viewModel.profileCompany.observe(this) { company ->
                binding.apply {
                    Glide.with(this@DetailJobActivity)
                        .load(company?.url_photo)
                        .into(ivJobs)
                    tvPekerjaan.text = job.nama
                    tvJmlh.text = job.jmlh_butuh.toString()
                    tvDetail.text = job.deskripsi
                }
            }
        }

        viewModel.listLeaderBoard.observe(this) {
            setLeaderBoardData(it)
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
        listApply: List<ListApplyLowonganQuery.Apply?>
    ) {
        val filteredList = listApply.filter { it?.status !in listOf("Diterima", "Ditolak") }
        lifecycleScope.launch {
            val userDeferredList = listApply.mapNotNull { apply ->
                apply?.user_iduser?.let { viewModel.getProfileUserAsync(it) }
            }

            val userList = userDeferredList.map { it.await() }

            // Match users with corresponding Apply objects based on user_iduser
            val userMap = userList.associateBy { it?.iduser }

            // Update the adapter with the correct user list
            val updatedListApply = listApply.map { apply ->
                apply?.user_iduser?.let { userMap[it] }
            }

            adapter = UserAdapter(
                filteredList,
                updatedListApply,
                this@DetailJobActivity::onAcceptClick,
                this@DetailJobActivity::onRejectClick
            )
            binding.rvLeaderboard.layoutManager = LinearLayoutManager(this@DetailJobActivity)
            binding.rvLeaderboard.adapter = adapter
        }
    }

    private fun onAcceptClick(idUser: Int) {
        val pekerjaan = binding.tvPekerjaan.text.toString()
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        val idLowongan = intent.getIntExtra(EXTRA_ID, 0)
        viewModel.updateUserApplyStatus(idUser, idLowongan, "Diterima")
        showText(this, "Accepted")
        viewModel.createNotification(
            formattedDateTime,
            "Congratulation! You are accepted as $pekerjaan",
            idUser
        )
        val updatedListApply = adapter?.getListApply()?.toMutableList()
        updatedListApply?.removeAll { it?.user_iduser == idUser }
        adapter?.updateListApply(updatedListApply!!)
    }

    private fun onRejectClick(idUser: Int) {
        val pekerjaan = binding.tvPekerjaan.text.toString()
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        viewModel.updateUserApplyStatus(idUser, id, "Ditolak")
        showText(this, "Rejected")
        viewModel.createNotification(
            formattedDateTime,
            "We're sorry that you are rejected as $pekerjaan",
            idUser
        )
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