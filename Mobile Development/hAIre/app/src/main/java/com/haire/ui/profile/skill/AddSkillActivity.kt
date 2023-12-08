package com.haire.ui.profile.skill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.haire.R

class AddSkillActivity : AppCompatActivity() {

    private lateinit var editTextSkill: EditText
    private lateinit var btnAddSkill: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_skill)

        editTextSkill = findViewById(R.id.editTextSkill)
        btnAddSkill = findViewById(R.id.btnAddSkill)

        btnAddSkill.setOnClickListener {
            val skill = editTextSkill.text.toString().trim()

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