package com.haire.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.haire.JobRepository
import com.haire.MainActivity
import com.haire.R
import com.haire.data.UserModel
import com.haire.data.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: JobRepository) : ViewModel() {

    val toastMsg:LiveData<String> = repository.toastMsg
    val success: LiveData<Boolean> = repository.success
    val isCompany: LiveData<Boolean> = repository.isCompany
    val id: LiveData<Int> = repository.id

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