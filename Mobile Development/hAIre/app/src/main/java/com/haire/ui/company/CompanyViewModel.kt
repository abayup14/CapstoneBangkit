package com.haire.ui.company

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.ListLowonganCompanyQuery
import com.haire.ListSkillsQuery
import com.haire.ProfileCompanyQuery
import com.haire.data.UserModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CompanyViewModel(private val repository: JobRepository) : ViewModel() {
    val success: LiveData<Boolean> = repository.success
    val toastMsg: LiveData<String> = repository.toastMsg
    val lokerCompany: LiveData<List<ListLowonganCompanyQuery.Lowongan?>> = repository.lokerCompany
    val listSkill: LiveData<List<ListSkillsQuery.Skill?>> = repository.listSkill
    val id: LiveData<Int> = repository.id
    val isLoading: LiveData<Boolean> = repository.isLoading

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

    fun createSkillRequired(idLowongan: Int, idSkill: Int) {
        viewModelScope.launch {
            repository.createSkillRequired(idLowongan, idSkill)
        }
    }

    fun getLokerCompany(idCompany: Int?) {
        viewModelScope.launch {
            repository.getLokerCompany(idCompany!!)
        }
    }

    fun listSkills() {
        viewModelScope.launch {
            repository.listSkill()
        }
    }

    fun checkSkill(nama: String) {
        viewModelScope.launch {
            repository.checkSkill(nama)
        }
    }

    fun saveProfile(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) =
        repository.saveProfile(imageUri, onSuccess, onFailure)

    fun updateDatabaseCompany(
        idCompany: Int,
        imageUrl: String,
        deskripsi: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            repository.updateDatabaseCompany(idCompany, imageUrl, deskripsi, onSuccess, onFailure)
        }
    }

    fun getProfileCompanyAsync(companyId: Int): Deferred<ProfileCompanyQuery.Company?> {
        return viewModelScope.async {
            repository.getCompanyData(companyId)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
