package com.haire.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.data.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: JobRepository) : ViewModel() {

    val toastMsg: LiveData<String> = repository.toastMsg
    val success: LiveData<Boolean> = repository.success
    val isCompany: LiveData<Boolean> = repository.isCompany
    val id: LiveData<Int> = repository.id
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            repository.saveUser(user)
        }
    }

    fun loginAcc(email: String, password: String) {
        viewModelScope.launch {
            repository.loginAccount(email, password)
        }
    }
}