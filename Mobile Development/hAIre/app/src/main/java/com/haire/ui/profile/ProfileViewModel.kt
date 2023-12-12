package com.haire.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.data.UserModel
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(private val repository: JobRepository) : ViewModel() {
    val profileAccount = repository.profileAccount
    fun getProfileUI(email: String) = repository.getProfileAccount(email)

    fun getUser(): LiveData<UserModel> = repository.getUser().asLiveData()

    fun deleteAccount(email: String) = repository.deleteAccount(email)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun saveProfile(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) = repository.saveProfile(imageUri, onSuccess, onFailure)
    fun updateDatabase(email: String, description: String, age: Int, imageUrl: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) = repository.updateDatabase(email, description, age, imageUrl, onSuccess, onFailure)
}