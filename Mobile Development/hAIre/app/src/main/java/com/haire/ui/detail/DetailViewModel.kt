package com.haire.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haire.GetLowonganQuery
import com.haire.JobRepository
import com.haire.ProfileCompanyQuery
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: JobRepository) : ViewModel() {
    val detailLowongan: LiveData<GetLowonganQuery.Lowongan?> = repository.detailLowongan
    val companyData: LiveData<ProfileCompanyQuery.Company?> = repository.profileCompany

    fun getDetail(id: Int) {
        viewModelScope.launch {
            repository.getLoker(id)
        }
    }

    fun getCompany(id: Int) {
        viewModelScope.launch {
            repository.getCompanyData(id)
        }
    }
}