package com.haire.ui.profile.education

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.haire.R

class AddEducationActivity : AppCompatActivity() {

    private lateinit var editTextDegree: EditText
    private lateinit var editTextSchoolName: EditText
    private lateinit var editTextFieldOfStudy: EditText
    private lateinit var tvEducationStartDate: TextView
    private lateinit var tvEducationEndDate: TextView
    private lateinit var btnAddEducation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_education)

        editTextDegree = findViewById(R.id.editTextDegree)
        editTextSchoolName = findViewById(R.id.editTextSchoolName)
        editTextFieldOfStudy = findViewById(R.id.editTextFieldOfStudy)
        tvEducationStartDate = findViewById(R.id.tv_start_date)
        tvEducationEndDate = findViewById(R.id.tv_end_date)
        btnAddEducation = findViewById(R.id.btnAddEducation)

        btnAddEducation.setOnClickListener {
            val degree = editTextDegree.text.toString().trim()
            val schoolName = editTextSchoolName.text.toString().trim()
            val fieldOfStudy = editTextFieldOfStudy.text.toString().trim()

            if (degree.isNotEmpty() && schoolName.isNotEmpty() && fieldOfStudy.isNotEmpty()) {
                // Add logic to submit the education to LinkedIn or perform other actions
                Toast.makeText(this, "Education added: $degree at $schoolName", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after adding the education
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}