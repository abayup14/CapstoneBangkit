package com.haire.ui.profile.experience

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.haire.R

class AddExperienceActivity : AppCompatActivity() {

    private lateinit var editTextJobTitle: EditText
    private lateinit var editTextCompanyName: EditText
    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView
    private lateinit var editTextDescription: EditText
    private lateinit var btnAddExperience: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_experience)

        editTextJobTitle = findViewById(R.id.editTextJobTitle)
        editTextCompanyName = findViewById(R.id.editTextCompanyName)
        tvStartDate = findViewById(R.id.tv_start_date)
        tvEndDate = findViewById(R.id.tv_end_date)
        editTextDescription = findViewById(R.id.editTextDescription)
        btnAddExperience = findViewById(R.id.btnAddExperience)

        btnAddExperience.setOnClickListener {
            val jobTitle = editTextJobTitle.text.toString().trim()
            val companyName = editTextCompanyName.text.toString().trim()
            val startDate = tvStartDate.text.toString().trim()
            val endDate = tvEndDate.text.toString().trim()
            val description = editTextDescription.text.toString().trim()

            if (jobTitle.isNotEmpty() && companyName.isNotEmpty() && description.isNotEmpty()) {
                // Add logic to submit the experience to LinkedIn or perform other actions
                Toast.makeText(this, "Experience added: $jobTitle at $companyName", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after adding the experience
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}