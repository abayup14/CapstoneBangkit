package com.haire.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.data.Company
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: JobRepository) : ViewModel() {

    val success: LiveData<Boolean> = repository.success

    fun registerAccount(
        nama: String?,
        email: String?,
        password: String?,
        nomor: String?,
        tgl: String?,
        nik: String?
    ) {
        viewModelScope.launch {
            repository.registerAccount(nama, email, password, nomor, tgl, nik)
        }
    }

    fun registerCompany(company: Company, password: String) =
        repository.registerCompany(company, password)
}