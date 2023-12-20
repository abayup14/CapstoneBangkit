package com.haire.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: JobRepository) : ViewModel() {

    val success: LiveData<Boolean> = repository.success
    val toastMsg: LiveData<String> = repository.toastMsg
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun registerAccount(
        nama: String?,
        email: String?,
        password: String?,
        nomor: String?,
        tgl: String?,
        nik: String?,
        disabled: Boolean?,
        homeless: Boolean?
    ) {
        viewModelScope.launch {
            repository.registerAccount(nama, email, password, nomor, tgl, nik, disabled, homeless)
        }
    }

    fun registerCompany(nama: String, alamat: String, email: String, password: String) {
        viewModelScope.launch {
            repository.registerCompany(nama, alamat, email, password)
        }
    }
}