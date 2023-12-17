package com.haire.ui.profile.skill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.haire.R

class AddSkillActivity : AppCompatActivity() {

    private lateinit var btnAddSkill: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_skill)

        btnAddSkill = findViewById(R.id.btnAddSkill)

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.edt_skill)

        val skillSuggestions = arrayOf("Java", "Android", "Kotlin", "HTML", "MySQL", "Unity", "C#", "C++")

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, skillSuggestions)
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedSkill = adapter.getItem(position).toString()
            // Lakukan sesuatu dengan skill yang dipilih, misalnya tambahkan ke daftar skill
//            addSkillToLayout(selectedSkill)
            autoCompleteTextView.setText(selectedSkill)
        }

        btnAddSkill.setOnClickListener {
            val skill = autoCompleteTextView.text.toString().trim()

            if (skill.isNotEmpty()) {
                // Add logic to submit the skill to LinkedIn or perform other actions
                Toast.makeText(this, "Skill added: $skill", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after adding the skill
            } else {
                Toast.makeText(this, "Please enter a skill", Toast.LENGTH_SHORT).show()
            }
        }

    }
}