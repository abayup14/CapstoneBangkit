package com.haire.ui.openjobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.ListLowongansQuery
import kotlinx.coroutines.launch

class OpenJobsViewModel(private val repository: JobRepository) : ViewModel() {
    val loker: LiveData<List<ListLowongansQuery.Lowongan?>> = repository.loker

    fun getListLoker() {
        viewModelScope.launch {
            repository.getListLoker()
        }
    }
}