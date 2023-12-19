package com.haire.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.ListApplyLowonganQuery
import com.haire.ListApplyUserQuery
import com.haire.ListLowonganUserApplyQuery
import com.haire.ProfileCompanyQuery
import com.haire.data.UserModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class StatusViewModel(private val repository: JobRepository) : ViewModel() {

    val listLoker: LiveData<List<ListLowonganUserApplyQuery.Lowongan?>> =
        repository.listLowonganUserApply
    val isLoading: LiveData<Boolean> = repository.isLoading
    val listApplyUser: LiveData<List<ListApplyUserQuery.Apply?>> = repository.listStatus

    fun getListLokerStatus(idUser: Int) {
        viewModelScope.launch {
            repository.getListLowonganUserApply(idUser)
        }
    }

    fun getProfileCompanyAsync(companyId: Int): Deferred<ProfileCompanyQuery.Company?> {
        return viewModelScope.async {
            repository.getCompanyData(companyId)
        }
    }

    fun getApplyStatusAsync(userId: Int) {
        viewModelScope.launch {
            repository.listApplyUser(userId)
        }
    }

    fun getSession(): LiveData<UserModel> = repository.getUser().asLiveData()
}