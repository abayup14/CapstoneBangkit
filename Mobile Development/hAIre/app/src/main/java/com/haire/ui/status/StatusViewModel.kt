package com.haire.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.ListLowonganUserApplyQuery
import com.haire.data.InitialDummyValue
import com.haire.data.Status
import com.haire.data.UserModel
import kotlinx.coroutines.launch

class StatusViewModel(private val repository: JobRepository) : ViewModel() {

    val listLoker: LiveData<List<ListLowonganUserApplyQuery.Lowongan?>> = repository.listLowonganUserApply

    fun getListLokerStatus(idUser: Int) {
        viewModelScope.launch {
            repository.getListLowonganUserApply(idUser)
        }
    }

    fun getSession(): LiveData<UserModel> = repository.getUser().asLiveData()
}