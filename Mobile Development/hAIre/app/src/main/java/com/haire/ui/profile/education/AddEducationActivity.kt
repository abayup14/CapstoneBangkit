package com.haire.ui.profile.education

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.haire.R

class AddEducationActivity : AppCompatActivity() {

    private lateinit var editTextDegree: EditText
    private lateinit var editTextSchoolName: EditText
    private lateinit var editTextFieldOfStudy: EditText
    private lateinit var editTextEducationDate: EditText
    private lateinit var btnAddEducation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_education)

        editTextDegree = findViewById(R.id.editTextDegree)
        editTextSchoolName = findViewById(R.id.editTextSchoolName)
        editTextFieldOfStudy = findViewById(R.id.editTextFieldOfStudy)
        editTextEducationDate = findViewById(R.id.editTextEducationDate)
        btnAddEducation = findViewById(R.id.btnAddEducation)

        btnAddEducation.setOnClickListener {
            val degree = editTextDegree.text.toString().trim()
            val schoolName = editTextSchoolName.text.toString().trim()
            val fieldOfStudy = editTextFieldOfStudy.text.toString().trim()
            val educationDate = editTextEducationDate.text.toString().trim()

            if (degree.isNotEmpty() && schoolName.isNotEmpty() && fieldOfStudy.isNotEmpty() && educationDate.isNotEmpty()) {
                // Add logic to submit the education to LinkedIn or perform other actions
                Toast.makeText(this, "Education added: $degree at $schoolName", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after adding the education
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}