package com.haire.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.ListNotifikasiUserQuery
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: JobRepository) : ViewModel() {
    val success:LiveData<Boolean> = repository.success
    val toastMsg: LiveData<String> = repository.toastMsg
    val isLoading: LiveData<Boolean> = repository.isLoading
    val listNotification: LiveData<List<ListNotifikasiUserQuery.Notifikasi?>> = repository.notifikasi

    fun getNotification(idUser: Int) {
        viewModelScope.launch {
            repository.getNotification(idUser)
        }
    }

    fun getSession() = repository.getUser().asLiveData()
}