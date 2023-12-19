package com.haire.ui.profile

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haire.JobRepository
import com.haire.ListEdukasiQuery
import com.haire.ListPengalamanQuery
import com.haire.ListSkillsQuery
import com.haire.ListUserSkillsQuery
import com.haire.ProfileUserQuery
import com.haire.data.UserModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: JobRepository) : ViewModel() {
    val profileData: LiveData<ProfileUserQuery.User> = repository.profileData
    val success: LiveData<Boolean> = repository.success
    val toastMsg: LiveData<String> = repository.toastMsg
    val exp: LiveData<List<ListPengalamanQuery.Pengalaman?>> = repository.exp
    val edu: LiveData<List<ListEdukasiQuery.Edukasi?>> = repository.edu
    val skill: LiveData<List<ListUserSkillsQuery.Skill?>> = repository.skill
    val listSkill: LiveData<List<ListSkillsQuery.Skill?>> = repository.listSkill
    val id: LiveData<Int> = repository.id
    val isLoading: LiveData<Boolean> = repository.isLoading

    @RequiresApi(Build.VERSION_CODES.O)
    fun getProfileUI(id: Int) {
        viewModelScope.launch {
            repository.getProfileData(id)
        }
    }

    fun getEdukasi(id: Int) {
        viewModelScope.launch {
            repository.getEdukasi(id)
        }
    }

    fun getSkills(id: Int) {
        viewModelScope.launch {
            repository.getSkills(id)
        }
    }

    fun listSkills() {
        viewModelScope.launch {
            repository.listSkill()
        }
    }

    fun createUserHasSkill(userId: Int, skillId: Int) {
        viewModelScope.launch {
            repository.createUserHasSkill(userId, skillId)
        }
    }

    fun checkSkill(nama: String) {
        viewModelScope.launch {
            repository.checkSkill(nama)
        }
    }

    fun updateEdukasi(id: Int, edu: String) {
        viewModelScope.launch {
            repository.updateEdukasi(id, edu)
        }
    }

    fun getPengalaman(id: Int) {
        viewModelScope.launch {
            repository.getPengalaman(id)
        }
    }

    fun createPengalaman(
        nama_pekerjaan: String,
        tgl_mulai: String,
        tgl_selesai: String,
        tmpt_bekerja: String,
        pkrjn_profesional: Boolean,
        user_iduser: Int
    ) {
        viewModelScope.launch {
            repository.createPengalaman(
                nama_pekerjaan,
                tgl_mulai,
                tgl_selesai,
                tmpt_bekerja,
                pkrjn_profesional,
                user_iduser
            )
        }
    }

    fun updateUser(
        idUser: Int,
        pengalaman: Int,
        pengalamanPro: Int
    ) {
        viewModelScope.launch {
            repository.updateUser(idUser, pengalaman, pengalamanPro)
        }
    }

    fun createEdukasi(
        user_iduser: Int,
        nama_institusi: String,
        deskripsi: String,
        jenjang: String,
        tgl_awal: String,
        tgl_akhir: String
    ) {
        viewModelScope.launch {
            repository.createEdukasi(
                user_iduser,
                nama_institusi,
                deskripsi,
                jenjang,
                tgl_awal,
                tgl_akhir
            )
        }
    }

    fun getUser(): LiveData<UserModel> = repository.getUser().asLiveData()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun saveProfile(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) =
        repository.saveProfile(imageUri, onSuccess, onFailure)
    fun updateDatabase(idUser: Int, description: String, imageUrl: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            repository.updateDatabase(idUser, description, imageUrl, onSuccess, onFailure)
        }
    }
}