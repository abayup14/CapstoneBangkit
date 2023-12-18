package com.haire.ui.company.addjob

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.data.Skill
import com.haire.databinding.ActivityAddJobBinding
import com.haire.ui.company.CompanyViewModel
import com.haire.util.DatePickerFragment
import com.haire.util.showLoading
import com.haire.util.showText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddJobActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private lateinit var binding: ActivityAddJobBinding
    private val viewModel by viewModels<CompanyViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBar)
        }

        val data = intent.getParcelableExtra<Skill>(EXTRA_SKILL)
        if (data != null) {
            Global.listRequired.add(data)
        }

        viewModel.success.observe(this) {
            if (it) {
                viewModel.id.observe(this) { idLowongan ->
                    for (a in Global.listRequired) {
                        viewModel.createSkillRequired(idLowongan, a.id)
                    }
                    Global.listRequired = arrayListOf()
                }
                showAlert()
            }
        }

        viewModel.toastMsg.observe(this) {
            showText(this, it)
        }

        binding.btnAddSkill.setOnClickListener {
            startActivity(Intent(this@AddJobActivity, AddSkillRequiredActivity::class.java))
            finish()
        }

        binding.btnStartDate.setOnClickListener {
            showDatePicker("dateStart")
        }
        binding.btnCloseDate.setOnClickListener {
            showDatePicker("dateEnd")
        }

        setSkillData(Global.listRequired)

        binding.btnAdd.setOnClickListener {
            val pekerjaan = binding.edtJob.text.toString()
            val deskripsi = binding.edtDeskripsi.text.toString()
            val jmlButuh = binding.edtButuh.text.toString()
            val tglAwal = binding.tvStartDate.text.toString()
            val tglAkhir = binding.tvCloseDate.text.toString()


            if (pekerjaan.isEmpty() || deskripsi.isEmpty() || jmlButuh.isEmpty()) {
                showText(this, "All field must be filled")
            } else {
                val butuhInt = jmlButuh.toInt()
                viewModel.getSession().observe(this) {
                    viewModel.createLoker(
                        name = pekerjaan,
                        desc = deskripsi,
                        butuh = butuhInt,
                        idCompany = it.id,
                        photoUrl = ""
                    )
                }
            }
        }
    }

    private fun showAlert() {
        AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.add_loker_succes))
            setTitle("Add Job")
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                finish()
            }
            show()
        }
    }

    private fun setSkillData(listSkill: List<Skill>) {
        for (skill in listSkill) {
            val newChip = Chip(this)
            newChip.text = skill.nama

            // Atur parameter layout untuk Chip
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.marginEnd = 8
            newChip.layoutParams = layoutParams

            binding.skills.addView(newChip)
        }
    }

    private fun showDatePicker(tag: String?) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, tag)
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        when (tag) {
            "dateStart" -> binding.tvStartDate.text = dateFormat.format(calendar.time)
            "dateEnd" -> binding.tvCloseDate.text = dateFormat.format(calendar.time)
        }
    }

    companion object {
        const val EXTRA_SKILL = "extra_skill"
    }
}