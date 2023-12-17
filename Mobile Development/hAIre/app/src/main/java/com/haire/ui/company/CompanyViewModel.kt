package com.haire.ui.company

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.ListLowonganCompanyQuery
import com.haire.data.Company
import com.haire.data.InitialDummyValue
import com.haire.data.Jobs
import com.haire.data.UserModel
import kotlinx.coroutines.launch

class CompanyViewModel(private val repository: JobRepository) : ViewModel() {
    val success: LiveData<Boolean> = repository.success
    val toastMsg: LiveData<String> = repository.toastMsg
    val lokerCompany: LiveData<List<ListLowonganCompanyQuery.Lowongan?>> = repository.lokerCompany

    fun getSession(): LiveData<UserModel> {
        return repository.getUser().asLiveData()
    }

    fun createLoker(
        name: String?,
        desc: String?,
        butuh: Int?,
        idCompany: Int?,
        photoUrl: String?
    ) {
        viewModelScope.launch {
            repository.createLoker(name, desc, butuh, idCompany, photoUrl)
        }
    }

    fun getLokerCompany(idCompany: Int?) {
        viewModelScope.launch {
            repository.getLokerCompany(idCompany!!)
        }
    }

    fun saveProfile(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) =
        repository.saveProfile(imageUri, onSuccess, onFailure)

//    fun updateDatabaseCompany(
//        email: String,
//        description: String,
//        imageUrl: String,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) = repository.updateDatabaseCompany(email, description, imageUrl, onSuccess, onFailure)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}