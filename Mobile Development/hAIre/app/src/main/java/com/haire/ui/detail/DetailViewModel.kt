package com.haire.ui.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haire.GetLowonganQuery
import com.haire.JobRepository
import com.haire.ListLowonganUserApplyQuery
import com.haire.ListSkillRequiredQuery
import com.haire.ProfileCompanyQuery
import com.haire.data.UserModel
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: JobRepository) : ViewModel() {
    val detailLowongan: LiveData<GetLowonganQuery.Lowongan?> = repository.detailLowongan
    val companyData: LiveData<ProfileCompanyQuery.Company?> = repository.profileCompany
    val skillRequired: LiveData<List<ListSkillRequiredQuery.Skill?>> = repository.skillRequired
    val success: LiveData<Boolean> = repository.success
    val isLoading: LiveData<Boolean> = repository.isLoading
    val listLoker: LiveData<List<ListLowonganUserApplyQuery.Lowongan?>> =
        repository.listLowonganUserApply

    fun getDetail(id: Int) {
        viewModelScope.launch {
            repository.getLoker(id)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun apply() {
        viewModelScope.launch {
            repository.apply()
        }
    }

    fun getUserProfile(idUser: Int) {
        viewModelScope.launch {
            repository.getProfileData(idUser)
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

    fun getListApplied(idUser: Int) {
        viewModelScope.launch {
            repository.getListLowonganUserApply(idUser)
        }
    }

    fun getSession(): LiveData<UserModel> = repository.getUser().asLiveData()
}