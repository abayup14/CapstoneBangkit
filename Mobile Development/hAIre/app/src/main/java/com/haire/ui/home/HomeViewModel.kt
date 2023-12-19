package com.haire.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.ListLowongansQuery
import com.haire.ProfileCompanyQuery
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: JobRepository) : ViewModel() {
    val loker: LiveData<List<ListLowongansQuery.Lowongan?>> = repository.loker
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun getListLoker() {
        viewModelScope.launch {
            repository.getListLoker("")
        }
    }

    fun getProfileCompanyAsync(companyId: Int): Deferred<ProfileCompanyQuery.Company?> {
        return viewModelScope.async {
            repository.getCompanyData(companyId)
        }
    }
}