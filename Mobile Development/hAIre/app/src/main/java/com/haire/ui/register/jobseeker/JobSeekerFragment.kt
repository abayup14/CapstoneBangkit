package com.haire.ui.register.jobseeker

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.databinding.FragmentJobSeekerBinding
import com.haire.ui.login.LoginActivity
import com.haire.ui.register.RegisterViewModel
import com.haire.util.showLoading
import com.haire.util.showText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class JobSeekerFragment : Fragment() {
    private var _binding: FragmentJobSeekerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel> { ViewModelFactory(requireContext()) }

    private var homeless = false
    private var disabled = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobSeekerBinding.inflate(inflater, container, false)
        binding.homelessRg.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton: RadioButton = requireActivity().findViewById(checkedId)
            when (selectedRadioButton.text.toString()) {
                "Yes" -> homeless = false
                "No" -> homeless = true
            }
        }

        binding.disabledRg.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton: RadioButton = requireActivity().findViewById(checkedId)
            when (selectedRadioButton.text.toString()) {
                "Yes" -> disabled = true
                "No" -> disabled = false
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it, binding.progressBar)
        }

        viewModel.success.observe(viewLifecycleOwner) {
            if (it) {
                showAlert()
            } else {
                showText(requireContext(), "Gagal")
            }
        }

        binding.btnEdtBirth.setOnClickListener {
            showDatePicker()
        }

        val validation = AwesomeValidation(ValidationStyle.BASIC)
        validation.apply {
            addValidation(
                binding.edtEmail,
                Patterns.EMAIL_ADDRESS,
                getString(R.string.invalid_email)
            )
            addValidation(binding.edtPassword, ".{6,}", getString(R.string.invalid_password))
            addValidation(
                binding.edtRePassword,
                binding.edtPassword,
                getString(R.string.password_not_match)
            )
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val pass = binding.edtPassword.text.toString()
            val rePass = binding.edtRePassword.text.toString()
            val phone = binding.edtPhone.text.toString()
            val birthDate = binding.tvBirthDate.text.toString()
            val nik = binding.edtNik.text.toString()
            val homelessIsChecked = binding.homelessRg.checkedRadioButtonId
            val disabledIsChecked = binding.disabledRg.checkedRadioButtonId

            if (validation.validate()) {
                if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || rePass.isEmpty() || phone.isEmpty() || birthDate.isEmpty() || nik.isEmpty() || homelessIsChecked == -1 || disabledIsChecked == -1) {
                    showText(requireContext(), getString(R.string.empty_field))
                } else {
                    viewModel.registerAccount(
                        name,
                        email,
                        pass,
                        phone,
                        birthDate,
                        nik,
                        disabled,
                        homeless
                    )
                }
            }
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
        return binding.root
    }

    private fun showAlert() {
        AlertDialog.Builder(requireActivity()).apply {
            setMessage(getString(R.string.register_success))
            setTitle("Register")
            setPositiveButton(getString(R.string.login)) { _, _ ->
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finish()
            }
            show()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                val selectedDate = selectedCalendar.time

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = sdf.format(selectedDate)
                binding.tvBirthDate.text = formattedDate
            },
            year, month, day
        )

        datePickerDialog.show()
    }
}