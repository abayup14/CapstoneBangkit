package com.haire.ui.openjobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.ListLowongansQuery
import com.haire.ProfileCompanyQuery
import kotlinx.coroutines.launch

class OpenJobsViewModel(private val repository: JobRepository) : ViewModel() {
    val loker: LiveData<List<ListLowongansQuery.Lowongan?>> = repository.loker
    val profileCompany: LiveData<ProfileCompanyQuery.Company?> = repository.profileCompany
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun getListLoker() {
        viewModelScope.launch {
            repository.getListLoker()
        }
    }

    fun getProfileCompany(id: Int) {
        viewModelScope.launch {
            repository.getCompanyData(id)
        }
    }
}