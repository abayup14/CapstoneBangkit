package com.haire.ui.profile.experience

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.databinding.ActivityAddExperienceBinding
import com.haire.ui.profile.ProfileViewModel
import com.haire.util.DatePickerFragment
import com.haire.util.showText
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale

class AddExperienceActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {

    private lateinit var editTextJobTitle: EditText
    private lateinit var editTextCompanyName: EditText
    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView
    private lateinit var editTextDescription: EditText
    private lateinit var btnAddExperience: Button
    private lateinit var binding: ActivityAddExperienceBinding
    private val viewModel by viewModels<ProfileViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        editTextJobTitle = findViewById(R.id.editTextJobTitle)
        editTextCompanyName = findViewById(R.id.editTextCompanyName)
        tvStartDate = findViewById(R.id.tv_start_date)
        tvEndDate = findViewById(R.id.tv_end_date)
        editTextDescription = findViewById(R.id.editTextDescription)
        btnAddExperience = findViewById(R.id.btnAddExperience)

        binding.btnStartDate.setOnClickListener {
            showDatePicker("dateStart")
        }
        binding.btnEndDate.setOnClickListener {
            showDatePicker("dateEnd")
        }

        viewModel.success.observe(this) {
            if (it) {
                finish()
            }
        }

        viewModel.toastMsg.observe(this) {
            showText(this, it)
        }

        btnAddExperience.setOnClickListener {
            val jobTitle = editTextJobTitle.text.toString().trim()
            val companyName = editTextCompanyName.text.toString().trim()
            val startDate = tvStartDate.text.toString().trim()
            val endDate = tvEndDate.text.toString().trim()
            val description = editTextDescription.text.toString().trim()
            val selectedSpinner = binding.spinner.selectedItem
            var pengalamanPro = false
            when (selectedSpinner) {
                "Pengalaman Non-Professional" -> pengalamanPro = false
                "Pengalaman Professional" -> pengalamanPro = true
            }

            if (jobTitle.isNotEmpty() && companyName.isNotEmpty() && description.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()) {
                viewModel.getUser().observe(this) {
                    viewModel.createPengalaman(
                        jobTitle,
                        startDate,
                        endDate,
                        companyName,
                        pengalamanPro,
                        it.id
                    )
                    // Format tanggal
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                    val date1 = LocalDate.parse(startDate, formatter)
                    val date2 = LocalDate.parse(endDate, formatter)

                    val selisihTahun = ChronoUnit.YEARS.between(date1, date2).toInt()
                    if (pengalamanPro) {
                        viewModel.updateUser(it.id, 0, selisihTahun)
                    } else {
                        viewModel.updateUser(it.id, selisihTahun, 0)
                    }
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
            "dateStart" -> tvStartDate.text = dateFormat.format(calendar.time)
            "dateEnd" -> tvEndDate.text = dateFormat.format(calendar.time)
        }
    }
}