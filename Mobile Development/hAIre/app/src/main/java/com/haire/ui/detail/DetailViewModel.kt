package com.haire.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haire.GetLowonganQuery
import com.haire.JobRepository
import com.haire.ListSkillRequiredQuery
import com.haire.ProfileCompanyQuery
import com.haire.data.UserModel
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: JobRepository) : ViewModel() {
    val detailLowongan: LiveData<GetLowonganQuery.Lowongan?> = repository.detailLowongan
    val companyData: LiveData<ProfileCompanyQuery.Company?> = repository.profileCompany
    val skillRequired: LiveData<List<ListSkillRequiredQuery.Skill?>> = repository.skillRequired
    val jaccard: LiveData<Double> = repository.jaccard

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

    fun getSkillRequired(idLowongan: Int) {
        viewModelScope.launch {
            repository.getSkillsRequired(idLowongan)
        }
    }

    fun getJaccardSkill(idUser: Int, idLowongan: Int) {
        viewModelScope.launch {
            repository.jaccardSkills(idUser, idLowongan)
        }
    }

    fun getSession(): LiveData<UserModel> = repository.getUser().asLiveData()
}