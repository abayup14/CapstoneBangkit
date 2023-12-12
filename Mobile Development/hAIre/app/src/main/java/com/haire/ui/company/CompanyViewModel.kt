package com.haire.ui.company

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.data.Company
import com.haire.data.InitialDummyValue
import com.haire.data.Jobs
import com.haire.data.UserModel
import kotlinx.coroutines.launch

class CompanyViewModel(private val repository: JobRepository) : ViewModel() {
    val vacancy: LiveData<List<Jobs>> = repository.jobVacancy
    val companyData: LiveData<Company> = repository.companyData

    fun getJob(email: String) = repository.getJobVacancy(email) // Error

    fun addJob(jobs: Jobs) = repository.addJob(jobs) // kosong

    fun getSession(): LiveData<UserModel> {
        return repository.getUser().asLiveData()
    }

    fun getCompanyData(email: String) = repository.getCompanyData(email)

    fun saveProfile(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) =
        repository.saveProfile(imageUri, onSuccess, onFailure)

    fun updateDatabaseCompany(
        email: String,
        description: String,
        imageUrl: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) = repository.updateDatabaseCompany(email, description, imageUrl, onSuccess, onFailure)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}