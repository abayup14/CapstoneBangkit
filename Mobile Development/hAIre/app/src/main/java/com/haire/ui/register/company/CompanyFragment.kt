package com.haire.ui.register.company

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.haire.R
import com.haire.ViewModelFactory
import com.haire.databinding.FragmentCompanyBinding
import com.haire.ui.login.LoginActivity
import com.haire.ui.register.RegisterViewModel
import com.haire.util.showText

class CompanyFragment : Fragment() {
    private var _binding: FragmentCompanyBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel> { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyBinding.inflate(inflater, container, false)
        viewModel.success.observe(viewLifecycleOwner) {
            if (it) {
                showAlert()
            }
        }
        viewModel.toastMsg.observe(viewLifecycleOwner) {
            showText(requireActivity(), it)
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
            val address = binding.edtAddress.text.toString()

            if (validation.validate()) {
                if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || rePass.isEmpty() || address.isEmpty()) {
                    showText(requireActivity(), getString(R.string.empty_field))
                } else {
                    viewModel.registerCompany(name, address, email, pass)
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
}