package com.haire

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.haire.data.UserModel

class MainViewModel(
    private val repository: JobRepository
) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getUser().asLiveData()
    }
}