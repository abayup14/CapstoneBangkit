package com.haire.ui.profile.skill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.ui.profile.ProfileViewModel
import com.haire.util.showText

class AddSkillActivity : AppCompatActivity() {

    private lateinit var btnAddSkill: Button
    private val viewModel by viewModels<ProfileViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_skill)

        btnAddSkill = findViewById(R.id.btnAddSkill)

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.edt_skill)

        viewModel.listSkills()
        viewModel.listSkill.observe(this) {
            val skillSuggestions = arrayListOf<String>()
            val skill = it
            for (a in skill) {
                skillSuggestions.add(a?.nama!!)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, skillSuggestions)
            autoCompleteTextView.setAdapter(adapter)

            viewModel.toastMsg.observe(this) { msg ->
                showText(this, msg)
            }

            autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
                val selectedSkill = adapter.getItem(position).toString()
                autoCompleteTextView.setText(selectedSkill)
            }
        }

        btnAddSkill.setOnClickListener {
            val skill = autoCompleteTextView.text.toString().trim()

            if (skill.isNotEmpty()) {
                viewModel.checkSkill(skill)
                viewModel.getUser().observe(this) { user ->
                    viewModel.id.observe(this) { id ->
                        viewModel.success.observe(this) { success ->
                            if (success) {
                                viewModel.createUserHasSkill(user.id, id)
                                finish() // Close the activity after adding the skill
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please enter a skill", Toast.LENGTH_SHORT).show()
            }
        }

    }
}