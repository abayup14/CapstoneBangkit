package com.haire.ui.company.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haire.GetLowonganQuery
import com.haire.JobRepository
import com.haire.ListApplyLowonganQuery
import com.haire.ListSkillRequiredQuery
import com.haire.ProfileCompanyQuery
import com.haire.ProfileUserQuery
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailJobViewModel(private val repository: JobRepository) : ViewModel() {
    val detailLowongan: LiveData<GetLowonganQuery.Lowongan?> = repository.detailLowongan
    val success: LiveData<Boolean> = repository.success
    val toastMsg: LiveData<String> = repository.toastMsg
    val skill: LiveData<List<ListSkillRequiredQuery.Skill?>> = repository.skillRequired
    val listLeaderBoard: LiveData<List<ListApplyLowonganQuery.Apply?>> = repository.listLeaderBoard
    val isLoading: LiveData<Boolean> = repository.isLoading
    val profileCompany: LiveData<ProfileCompanyQuery.Company?> = repository.profileCompany

    fun getLowongan(id: Int) {
        viewModelScope.launch {
            repository.getLoker(id)
        }
    }

    fun getSkills(id: Int) {
        viewModelScope.launch {
            repository.getSkillsRequired(id)
        }
    }

    fun updateUserApplyStatus(
        idUser: Int,
        idLowongan: Int,
        status: String
    ) {
        viewModelScope.launch {
            repository.updateUserApplyStatus(idUser, idLowongan, status)
        }
    }

    fun getListApplyLoker(id: Int) {
        viewModelScope.launch {
            repository.listApplyLowongan(id)
        }
    }
    fun listSkills() {
        viewModelScope.launch {
            repository.listSkill()
        }
    }

    fun getProfileCompany(idCompany: Int) {
        viewModelScope.launch {
            repository.getCompanyData(idCompany)
        }
    }

    fun getProfileUserAsync(idUser: Int): Deferred<ProfileUserQuery.User?> {
        return viewModelScope.async {
            repository.getProfileData(idUser)
        }
    }

    fun createNotification(waktu: String, pesan: String, idUser: Int) {
        viewModelScope.launch {
            repository.createNotification(waktu, pesan, idUser)
        }
    }
}