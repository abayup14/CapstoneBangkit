package com.haire.ui.register

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.haire.JobRepository
import com.haire.R
import com.haire.data.User
import com.haire.ui.login.LoginActivity

class RegisterViewModel(private val repository: JobRepository) : ViewModel() {

    val isEmailExist: LiveData<Boolean> = repository.success

    fun registerAccount(user: User, password: String) = repository.registerAccount(user, password)
}