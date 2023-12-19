package com.haire.ui.profile.education

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityAddEducationBinding
import com.haire.ui.profile.ProfileViewModel
import com.haire.util.DatePickerFragment
import com.haire.util.showLoading
import com.haire.util.showText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddEducationActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {

    private lateinit var editTextSchoolName: EditText
    private lateinit var editTextFieldOfStudy: EditText
    private lateinit var tvEducationStartDate: TextView
    private lateinit var tvEducationEndDate: TextView
    private lateinit var btnAddEducation: Button
    private lateinit var binding: ActivityAddEducationBinding
    private val viewModel by viewModels<ProfileViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEducationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        editTextSchoolName = findViewById(R.id.editTextSchoolName)
        editTextFieldOfStudy = findViewById(R.id.editTextFieldOfStudy)
        tvEducationStartDate = findViewById(R.id.tv_start_date)
        tvEducationEndDate = findViewById(R.id.tv_end_date)
        btnAddEducation = findViewById(R.id.btnAddEducation)

        binding.btnStartDate.setOnClickListener {
            showDatePicker("dateStart")
        }
        binding.btnEndDate.setOnClickListener {
            showDatePicker("dateEnd")
        }

        viewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBar)
        }

        viewModel.toastMsg.observe(this) {
            showText(this, it)
        }

        viewModel.success.observe(this) {
            if (it) {
                finish()
            }
        }

        btnAddEducation.setOnClickListener {
            val schoolName = editTextSchoolName.text.toString().trim()
            val fieldOfStudy = editTextFieldOfStudy.text.toString().trim()
            var selectedItem = binding.spinner.selectedItem.toString()
            val tglAwal = tvEducationStartDate.text.toString()
            val tglAkhir = tvEducationEndDate.text.toString()

            if (schoolName.isNotEmpty() && fieldOfStudy.isNotEmpty() && tglAwal.isNotEmpty() && tglAkhir.isNotEmpty()) {
                if (selectedItem == "HighSchool Or Below") {
                    selectedItem = "HighSchoolOrBelow"
                }
                viewModel.getUser().observe(this) {
                    viewModel.createEdukasi(
                        it.id,
                        schoolName,
                        fieldOfStudy,
                        selectedItem,
                        tglAwal,
                        tglAkhir
                    )
                    viewModel.updateEdukasi(it.id, selectedItem)
                }
            } else {
                showText(this, "Please fill in all fields")
            }
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
            "dateStart" -> tvEducationStartDate.text = dateFormat.format(calendar.time)
            "dateEnd" -> tvEducationEndDate.text = dateFormat.format(calendar.time)
        }

    }
}