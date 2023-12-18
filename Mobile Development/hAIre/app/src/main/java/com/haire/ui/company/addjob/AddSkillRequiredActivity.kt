package com.haire.ui.company.addjob

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.haire.ViewModelFactory
import com.haire.data.Skill
import com.haire.databinding.ActivityAddSkillBinding
import com.haire.ui.company.CompanyViewModel
import com.haire.util.showLoading
import com.haire.util.showText

class AddSkillRequiredActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSkillBinding
    private val viewModel by viewModels<CompanyViewModel> { ViewModelFactory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSkillBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBar)
        }

        viewModel.listSkills()
        viewModel.listSkill.observe(this) {
            val skillSuggestions = arrayListOf<String>()
            val skill = it
            for (a in skill) {
                skillSuggestions.add(a?.nama!!)
            }

            val adapter =
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, skillSuggestions)
            binding.edtSkill.setAdapter(adapter)

            viewModel.toastMsg.observe(this) { msg ->
                showText(this, msg)
            }

            binding.edtSkill.setOnItemClickListener { _, _, position, _ ->
                val selectedSkill = adapter.getItem(position).toString()
                binding.edtSkill.setText(selectedSkill)
            }
        }

        binding.btnAddSkill.setOnClickListener {
            val skill = binding.edtSkill.text.toString()

            if (skill.isNotEmpty()) {
                viewModel.checkSkill(skill)
                viewModel.id.observe(this) { id ->
                    val skill2 = Skill(id, skill)
                    val intentAddJob =
                        Intent(this@AddSkillRequiredActivity, AddJobActivity::class.java)
                    intentAddJob.putExtra(AddJobActivity.EXTRA_SKILL, skill2)
                    startActivity(intentAddJob)
                    finish()
                }
            } else {
                showText(this, "Please enter a skill")
            }
        }
    }
}