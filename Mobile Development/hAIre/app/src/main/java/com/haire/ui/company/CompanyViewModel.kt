package com.haire.ui.company

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

    fun getJob(email: String) = repository.getJobVacancy(email)

    fun addJob(jobs: Jobs) = repository.addJob(jobs)

    fun getSession(): LiveData<UserModel> {
        return repository.getUser().asLiveData()
    }

    fun getCompanyData(email: String) = repository.getCompanyData(email)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}