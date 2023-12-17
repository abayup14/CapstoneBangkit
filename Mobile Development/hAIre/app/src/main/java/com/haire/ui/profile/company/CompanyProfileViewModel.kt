package com.haire.ui.profile.company

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.ProfileCompanyQuery
import com.haire.data.UserModel
import kotlinx.coroutines.launch

class CompanyProfileViewModel(private val repository: JobRepository) : ViewModel() {
    val profileCompanyData: LiveData<ProfileCompanyQuery.Company> = repository.profileCompanyData
    val success: LiveData<Boolean> = repository.success
    val toastMsg: LiveData<String> = repository.toastMsg

    fun getProfileUI(id: Int) {
        viewModelScope.launch {
            repository.getProfileDataCompany(id)
        }
    }

    fun getUser(): LiveData<UserModel> = repository.getUser().asLiveData()
}