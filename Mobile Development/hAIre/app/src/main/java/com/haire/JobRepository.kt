package com.haire

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.google.firebase.storage.FirebaseStorage
import com.haire.data.UserModel
import com.haire.data.UserPreference
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.math.ln

class JobRepository(private val pref: UserPreference) {
    private val gcsBucketName = "haireproject"
    private val storage: Storage = StorageOptions.getDefaultInstance().service

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isCompany = MutableLiveData<Boolean>()
    val isCompany: LiveData<Boolean> = _isCompany

    private val _id = MutableLiveData<Int>()
    val id: LiveData<Int> = _id

    private val _skill = MutableLiveData<List<ListUserSkillsQuery.Skill?>>()
    val skill: LiveData<List<ListUserSkillsQuery.Skill?>> = _skill

    private val _skillRequired = MutableLiveData<List<ListSkillRequiredQuery.Skill?>>()
    val skillRequired: LiveData<List<ListSkillRequiredQuery.Skill?>> = _skillRequired

    private val _exp = MutableLiveData<List<ListPengalamanQuery.Pengalaman?>>()
    val exp: LiveData<List<ListPengalamanQuery.Pengalaman?>> = _exp

    private val _edu = MutableLiveData<List<ListEdukasiQuery.Edukasi?>>()
    val edu: LiveData<List<ListEdukasiQuery.Edukasi?>> = _edu

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> = _toastMsg

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _loker = MutableLiveData<List<ListLowongansQuery.Lowongan?>>()
    val loker: LiveData<List<ListLowongansQuery.Lowongan?>> = _loker

    private val _lokerCompany = MutableLiveData<List<ListLowonganCompanyQuery.Lowongan?>>()
    val lokerCompany: LiveData<List<ListLowonganCompanyQuery.Lowongan?>> = _lokerCompany

    private val _profileData = MutableLiveData<ProfileUserQuery.User>()
    val profileData: LiveData<ProfileUserQuery.User> = _profileData

    private val _profileCompanyData = MutableLiveData<ProfileCompanyQuery.Company>()
    val profileCompanyData: LiveData<ProfileCompanyQuery.Company> = _profileCompanyData

    private val _profileCompany = MutableLiveData<ProfileCompanyQuery.Company?>()
    val profileCompany: LiveData<ProfileCompanyQuery.Company?> = _profileCompany

    private val _detailLowongan = MutableLiveData<GetLowonganQuery.Lowongan?>()
    val detailLowongan: LiveData<GetLowonganQuery.Lowongan?> = _detailLowongan

    private val _listSkill = MutableLiveData<List<ListSkillsQuery.Skill?>>()
    val listSkill: LiveData<List<ListSkillsQuery.Skill?>> = _listSkill

    private val _listLeaderBoard = MutableLiveData<List<ListApplyLowonganQuery.Apply?>>()
    val listLeaderBoard: LiveData<List<ListApplyLowonganQuery.Apply?>> = _listLeaderBoard

    private val _listStatus = MutableLiveData<List<ListApplyUserQuery.Apply?>>()
    val listStatus: LiveData<List<ListApplyUserQuery.Apply?>> = _listStatus

    private val _notifikasi = MutableLiveData<List<ListNotifikasiUserQuery.Notifikasi?>>()
    val notifikasi: LiveData<List<ListNotifikasiUserQuery.Notifikasi?>> = _notifikasi

    var skillSize: Double = 0.0
    var userAge: Double = 0.0
    var pengalamanLog: Double = 0.0
    var pengalamanProLog: Double = 0.0
    var pengalamanTotal: Double = 0.0
    var edukasi: Double = 0.0
    var lessThan35: Double = 0.0
    var jaccard: Double = 0.0
    var lowonganId: Int = 0
    var tglLahir: String = ""
    var iduser: Int = 0

    private val _listLowonganUserApply =
        MutableLiveData<List<ListLowonganUserApplyQuery.Lowongan?>>()
    val listLowonganUserApply: LiveData<List<ListLowonganUserApplyQuery.Lowongan?>> =
        _listLowonganUserApply

    private val apolloClient = ApolloClient.Builder()
        .serverUrl("https://haire-wg2zo5xzsq-et.a.run.app/graphql")
        .build()

    suspend fun loginAccount(email: String, password: String) {
        val email = Optional.present(email)
        val pass = Optional.present(password)
        _isLoading.value = true
        val response =
            apolloClient.query(CekLoginUserQuery(email = email, password = pass))
                .execute()
        val response2 =
            apolloClient.query(CekLoginCompanyQuery(email = email, password = pass))
                .execute()
        if (response2.hasErrors()) {
            _toastMsg.value = "There's an error while connecting to server."
        } else {
            if (response2.data?.cekLoginCompany?.success == true) {
                _isLoading.value = false
                _success.value = false
                _isCompany.value = true
                _id.value = response2.data?.cekLoginCompany?.company?.id ?: 0
            } else {
                if (response.hasErrors()) {
                    _isLoading.value = false
                    _toastMsg.value = "There's an error while connecting to server."
                } else {
                    if (response.data?.cekLoginUser?.success == true) {
                        _isLoading.value = false
                        _isCompany.value = false
                        _success.value = true
                        _id.value = response.data?.cekLoginUser?.user?.iduser ?: 0
                    } else {
                        if (response.errors?.isNotEmpty() == true) {
                            _isLoading.value = false
                            _toastMsg.value = response.errors?.component1()?.message
                        } else {
                            _isLoading.value = false
                            _toastMsg.value = response.data?.cekLoginUser?.errors?.component1()
                        }
                    }
                }
            }
        }
    }

    suspend fun registerAccount(
        nama: String?,
        email: String?,
        password: String?,
        nomor: String?,
        tgl: String?,
        nik: String?,
        disabled: Boolean?,
        homeless: Boolean?
    ) {
        val name: Optional<String> = Optional.present(nama ?: "")
        val mail: Optional<String> = Optional.present(email ?: "")
        val pass: Optional<String> = Optional.present(password ?: "")
        val hp: Optional<String> = Optional.present(nomor ?: "")
        val lahir: Optional<String> = Optional.present(tgl ?: "")
        val nikk: Optional<String> = Optional.present(nik ?: "")
        val disable: Optional<Boolean> = Optional.present(disabled ?: false)
        val homeles: Optional<Boolean> = Optional.present(homeless ?: false)

        try {
            _isLoading.value = true
            val response = apolloClient.mutation(
                CreateUserMutation(
                    nama = name,
                    email = mail,
                    password = pass,
                    nomor = hp,
                    tgl = lahir,
                    nik = nikk,
                    homeless = homeles,
                    disabilitas = disable
                )
            ).execute()
            if (response.data?.createUser?.success == true) {
                _isLoading.value = false
                _success.value = true
            } else {
                _success.value = false
                if (response.errors?.isNotEmpty() == true) {
                    _isLoading.value = false
                    _toastMsg.value = response.errors?.component1()?.message
                } else {
                    _isLoading.value = false
                    _toastMsg.value = response.data?.createUser?.errors?.component1()
                }
            }
        } catch (e: ApolloException) {
            _isLoading.value = false
            _success.value = false
            _toastMsg.value = e.message.toString()
        }
    }

    suspend fun registerCompany(nama: String, alamat: String, email: String, password: String) {
        val nama = Optional.present(nama)
        val alamat = Optional.present(alamat)
        val mail = Optional.present(email)
        val pass = Optional.present(password)

        try {
            _isLoading.value = true
            val response =
                apolloClient.mutation(CreateCompanyMutation(nama, alamat, mail, pass)).execute()
            if (response.data?.createCompany?.success == true) {
                _isLoading.value = false
                _success.value = true
            } else {
                _success.value = false
                if (response.errors?.isNotEmpty() == true) {
                    _isLoading.value = false
                    _toastMsg.value = response.errors?.component1()?.message
                } else {
                    _isLoading.value = false
                    _toastMsg.value = response.data?.createCompany?.errors?.component1()
                }
            }
        } catch (e: ApolloException) {
            _isLoading.value = false
            _success.value = false
            _toastMsg.value = e.message.toString()
        }
    }

    suspend fun getListLowonganUserApply(id: Int) {
        val idUser = Optional.present(id)
        _isLoading.value = true
        val response = apolloClient.query(ListLowonganUserApplyQuery(idUser)).execute()
        if (response.data?.listLowonganUserApply?.success == true) {
            _isLoading.value = false
            _listLowonganUserApply.value =
                response.data?.listLowonganUserApply?.lowongan ?: emptyList()
        } else if (response.hasErrors()) {
            _isLoading.value = false
            _toastMsg.value = response.errors?.component1()?.message
        } else {
            _isLoading.value = false
            _toastMsg.value = response.data?.listLowonganUserApply?.errors?.component1()
        }
    }

    suspend fun getCompanyData(id: Int?): ProfileCompanyQuery.Company {
        val companyId = Optional.present(id)
        _isLoading.value = true
        val response = apolloClient.query(ProfileCompanyQuery(companyId)).execute()
        if (response.data?.profileCompany?.success == true) {
            _isLoading.value = false
            _profileCompany.value = response.data?.profileCompany?.company
        } else if (response.hasErrors()) {
            _isLoading.value = false
            _toastMsg.value = response.errors?.component1()?.message
        } else {
            _isLoading.value = false
            _toastMsg.value = response.data?.profileCompany?.errors?.component1()
        }
        return response.data?.profileCompany?.company!!
    }

    suspend fun getListLoker(search: String) {
        val query = Optional.present(search)
        _isLoading.value = true
        val response = apolloClient.query(ListLowongansQuery(query)).execute()
        if (response.data?.listLowonganUserSearch?.success == true) {
            _isLoading.value = false
            _loker.value = response.data?.listLowonganUserSearch?.lowongan ?: emptyList()
        } else {
            if (response.errors?.isNotEmpty() == true) {
                _isLoading.value = false
                _toastMsg.value = response.errors?.component1()?.message
            } else {
                _isLoading.value = false
                _toastMsg.value = response.data?.listLowonganUserSearch?.errors?.component1()
            }
        }
    }

    suspend fun updateUserApplyStatus(
        userId: Int,
        lowonganId: Int,
        status: String
    ) {
        val idUser = Optional.present(userId)
        val idLowongan = Optional.present(lowonganId)
        val stat = Optional.present(status)
        try {
            val response =
                apolloClient.mutation(UpdateUserApplyStatusMutation(idUser, idLowongan, stat))
                    .execute()
            if (response.data?.updateUserApplyStatus?.success == true) {
                _success.value = true
            } else if (response.hasErrors()) {
                _success.value = false
                _toastMsg.value = response.errors?.component1()?.message
            } else {
                _success.value = false
                _toastMsg.value = response.data?.updateUserApplyStatus?.errors?.component1()
            }
        } catch (e: ApolloException) {
            _success.value = false
            _toastMsg.value = e.message.toString()
        }
    }

    suspend fun checkSkill(nama: String) {
        val namaSkill = Optional.present(nama)
        val response = apolloClient.query(CheckSkillQuery(namaSkill)).execute()
        if (response.data?.checkSkill?.success == true) {
            _id.value = response.data?.checkSkill?.skill?.id ?: 0
            _success.value = true
        } else if (response.hasErrors()) {
            _toastMsg.value = response.errors?.component1()?.message
        } else {
            createSkill(nama)
        }
    }

    suspend fun createUserHasSkill(user_iduser: Int, skills_id: Int) {
        val userId = Optional.present(user_iduser)
        val skillId = Optional.present(skills_id)
        try {
            val response =
                apolloClient.mutation(CreateUserHasSkillsMutation(userId, skillId)).execute()
            if (response.hasErrors()) {
                _success.value = false
                _toastMsg.value = response.errors?.component1()?.message
            } else if (response.data?.createUserHasSkills?.errors?.isNotEmpty() == true) {
                _success.value = false
                _toastMsg.value = response.data?.createUserHasSkills?.errors?.component1()
            }
        } catch (e: ApolloException) {
            _success.value = false
            _toastMsg.value = e.message.toString()
        }
    }

    suspend fun getLoker(id: Int?) {
        val idLowongan = Optional.present(id)
        _isLoading.value = true
        val response = apolloClient.query(GetLowonganQuery(idLowongan)).execute()
        if (response.data?.getLowongan?.success == true) {
            _isLoading.value = false
            _detailLowongan.value = response.data?.getLowongan?.lowongan
            lowonganId = response.data?.getLowongan?.lowongan?.id ?: 0
        } else if (response.hasErrors()) {
            _isLoading.value = false
            _toastMsg.value = response.errors?.component1()?.message
        } else {
            _isLoading.value = false
            _toastMsg.value = response.data?.getLowongan?.errors?.component1()
        }
    }

    suspend fun listSkill() {
        _isLoading.value = true
        val response = apolloClient.query(ListSkillsQuery()).execute()
        if (response.data?.listSkills?.success == true) {
            _isLoading.value = false
            _listSkill.value = response.data?.listSkills?.skills ?: emptyList()
        } else if (response.hasErrors()) {
            _isLoading.value = false
            _toastMsg.value = response.errors?.component1()?.message
        } else {
            _isLoading.value = false
            _toastMsg.value = response.data?.listSkills?.errors?.component1()
        }
    }

    suspend fun getLokerCompany(companyId: Int) {
        _isLoading.value = true
        val idCompany = Optional.present(companyId)
        val response = apolloClient.query(ListLowonganCompanyQuery(idCompany)).execute()
        if (response.data?.listLowonganCompany?.success == true) {
            _isLoading.value = false
            _lokerCompany.value = response.data?.listLowonganCompany?.lowongan ?: emptyList()
        } else {
            if (response.errors?.isNotEmpty() == true) {
                _isLoading.value = false
                _toastMsg.value = response.errors?.component1()?.message
            } else {
                _isLoading.value = false
                _toastMsg.value = response.data?.listLowonganCompany?.errors?.component1()
            }
        }
    }

    suspend fun getProfileDataCompany(idUser: Int?) {
        _isLoading.value = true
        val userId = Optional.present(idUser)
        val response = apolloClient.query(ProfileCompanyQuery(userId)).execute()
        if (response.data?.profileCompany?.success == true) {
            _isLoading.value = false
            _profileCompanyData.value = response.data?.profileCompany?.company!!
        } else {
            _isLoading.value = false
            _toastMsg.value = response.data?.profileCompany?.errors?.component1()
        }
    }

    suspend fun getProfileData(idUser: Int?): ProfileUserQuery.User {
        _isLoading.value = true
        val userId = Optional.present(idUser)
        val response = apolloClient.query(ProfileUserQuery(userId)).execute()
        if (response.data?.profileUser?.success == true) {
            _isLoading.value = false
            _profileData.value = response.data?.profileUser?.user!!
            iduser = response.data?.profileUser?.user?.iduser!!
            tglLahir = response.data?.profileUser?.user?.tgl_lahir!!
            pengalamanLog =
                ln(response.data?.profileUser?.user?.pengalaman?.toDouble()?.plus(1) ?: 0.0)
            pengalamanProLog =
                ln(response.data?.profileUser?.user?.pengalaman_pro?.toDouble()?.plus(1) ?: 0.0)
            pengalamanTotal = pengalamanLog + pengalamanProLog
            when (response.data?.profileUser?.user?.edukasi) {
                "HighSchoolOrBelow" -> edukasi = 1.0
                "Other" -> edukasi = 2.0
                "Undergraduate" -> edukasi = 3.0
                "Master" -> edukasi = 4.0
                "PhD" -> edukasi = 5.0
            }
        } else {
            if (response.errors?.isNotEmpty() == true) {
                _isLoading.value = false
                _toastMsg.value = response.errors?.component1()?.message
            } else {
                _isLoading.value = false
                _toastMsg.value = response.data?.profileUser?.errors?.component1()
            }
        }
        return response.data?.profileUser?.user!!
    }

    suspend fun createApply(
        user_iduser: Int,
        lowongan_id: Int,
        probabilitas: Double,
        jaccard: Double,
        skor_akhir: Double,
        status: String
    ) {
        val idUser = Optional.present(user_iduser)
        val lowonganId = Optional.present(lowongan_id)
        val prob = Optional.present(probabilitas)
        val jaccard = Optional.present(jaccard)
        val skorAkhir = Optional.present(skor_akhir)
        val stat = Optional.present(status)
        val response = apolloClient.mutation(
            CreateApplyMutation(
                idUser,
                lowonganId,
                prob,
                jaccard,
                skorAkhir,
                stat
            )
        ).execute()
        if (response.hasErrors()) {
            _toastMsg.value = response.errors?.component1()?.message
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun apply() {
        _isLoading.value = true
        val hariIni: LocalDate = LocalDate.now()
        val pattern = "yyyy-MM-dd"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val tglLahir = LocalDate.parse(tglLahir, formatter)
        userAge = Period.between(tglLahir, hariIni).years.toDouble()
        lessThan35 = if (userAge > 35) {
            0.0
        } else {
            1.0
        }
        val parameter = Optional.present(listOf(lessThan35, edukasi, pengalamanTotal, skillSize))
        val predictApply = apolloClient.query(PredictApplyQuery(parameter)).execute()
        if (predictApply.data?.predictApply?.success == true) {
            _isLoading.value = false
            val probabilitas = predictApply.data?.predictApply?.prob
            val skorAkhir = jaccard * probabilitas!!
            createApply(iduser, lowonganId, probabilitas, jaccard, skorAkhir, "Apply")
            _success.value = true
        } else if (predictApply.hasErrors()) {
            _isLoading.value = false
            _toastMsg.value = predictApply.errors?.component1()?.message
        } else {
            _isLoading.value = false
            _toastMsg.value = predictApply.data?.predictApply?.errors?.component1()
        }
    }

    suspend fun listApplyLowongan(id: Int) {
        _isLoading.value = true
        val idLowongan = Optional.present(id)
        val response = apolloClient.query(ListApplyLowonganQuery(idLowongan)).execute()
        if (response.data?.listApplyLowongan?.success == true) {
            _isLoading.value = false
            _listLeaderBoard.value = response.data?.listApplyLowongan?.apply ?: emptyList()
        } else if (response.hasErrors()) {
            _isLoading.value = false
            _toastMsg.value = response.errors?.component1()?.message
        } else {
            _toastMsg.value = response.data?.listApplyLowongan?.errors?.component1()
        }
    }

    suspend fun listApplyUser(user_iduser: Int) {
        val userId = Optional.present(user_iduser)
        val response = apolloClient.query(ListApplyUserQuery(userId)).execute()
        if (response.data?.listApplyUser?.success == true) {
            _isLoading.value = false
            _listStatus.value = response.data?.listApplyUser?.apply ?: emptyList()
        } else if (response.hasErrors()) {
            _isLoading.value = false
            _toastMsg.value = response.errors?.component1()?.message
        } else {
            _toastMsg.value = response.data?.listApplyUser?.errors?.component1()
        }
    }

    suspend fun createNotification(waktu: String, pesan: String, user_iduser: Int) {
        val waktu = Optional.present(waktu)
        val message = Optional.present(pesan)
        val idUser = Optional.present(user_iduser)
        _isLoading.value = true
        val response =
            apolloClient.mutation(CreateNotificationMutation(waktu, message, idUser)).execute()
        if (response.data?.createNotification?.success == true) {
            _success.value = true
            _isLoading.value = false
        } else if (response.hasErrors()) {
            _isLoading.value = false
            _toastMsg.value = response.errors?.component1()?.message
        } else {
            _isLoading.value = false
            _toastMsg.value = response.data?.createNotification?.errors?.component1()
        }
    }

    suspend fun getNotification(idUser: Int) {
        val userId = Optional.present(idUser)
        val response = apolloClient.query(ListNotifikasiUserQuery(userId)).execute()
        _isLoading.value = true
        if (response.data?.listNotifikasiUser?.success == true) {
            _isLoading.value = false
            _notifikasi.value = response.data?.listNotifikasiUser?.notifikasi ?: emptyList()
        } else if (response.hasErrors()) {
            _isLoading.value = false
            _toastMsg.value = response.errors?.component1()?.message
        } else {
            _isLoading.value = false
            _toastMsg.value = response.data?.listNotifikasiUser?.errors?.component1()
        }
    }

    suspend fun getPengalaman(id: Int?) {
        _isLoading.value = true
        val userId = Optional.present(id)
        val response = apolloClient.query(ListPengalamanQuery(userId)).execute()
        if (response.data?.listPengalamanUser?.success == true) {
            _isLoading.value = false
            _exp.value = response.data?.listPengalamanUser?.pengalaman ?: emptyList()
        } else {
            if (response.errors?.isNotEmpty() == true) {
                _isLoading.value = false
                _toastMsg.value = response.errors?.component1()?.message
            } else {
                _isLoading.value = false
                _toastMsg.value = response.data?.listPengalamanUser?.errors?.component1()
            }
        }
    }

    suspend fun getEdukasi(id: Int?) {
        _isLoading.value = true
        val idUser = Optional.present(id)
        val response = apolloClient.query(ListEdukasiQuery(idUser)).execute()
        if (response.data?.listEdukasiUser?.success == true) {
            _isLoading.value = false
            _edu.value = response.data?.listEdukasiUser?.edukasi ?: emptyList()
        } else {
            if (response.errors?.isNotEmpty() == true) {
                _isLoading.value = false
                _toastMsg.value = response.errors?.component1()?.message
            } else {
                _isLoading.value = false
                _toastMsg.value = response.data?.listEdukasiUser?.errors?.component1()
            }
        }
    }

    suspend fun getSkills(id: Int?): List<String?> {
        _isLoading.value = true
        val idUser = Optional.present(id)
        val response = apolloClient.query(ListUserSkillsQuery(idUser)).execute()
        val listSkill = arrayListOf<String?>()
        if (response.data?.listUserSkills?.success == true) {
            _isLoading.value = false
            _skill.value = response.data?.listUserSkills?.skills ?: emptyList()
            skillSize = response.data?.listUserSkills?.skills?.size?.toDouble() ?: 0.0
            for (a in response.data?.listUserSkills?.skills!!) {
                listSkill.add(a?.nama)
            }
        } else {
            if (response.errors?.isNotEmpty() == true) {
                _isLoading.value = false
                _toastMsg.value = response.errors?.component1()?.message
            } else {
                _isLoading.value = false
                _toastMsg.value = response.data?.listUserSkills?.errors?.component1()
            }
        }
        return listSkill
    }

    suspend fun getSkillsRequired(id: Int?): List<String?> {
        _isLoading.value = true
        val idLowongan = Optional.present(id)
        val listSkillReq = arrayListOf<String?>()
        val response = apolloClient.query(ListSkillRequiredQuery(idLowongan)).execute()
        if (response.data?.listSkillsRequired?.success == true) {
            _isLoading.value = false
            _skillRequired.value = response.data?.listSkillsRequired?.skills ?: emptyList()
            for (a in response.data?.listSkillsRequired?.skills!!) {
                listSkillReq.add(a?.nama)
            }
        } else if (response.hasErrors()) {
            _isLoading.value = false
            _toastMsg.value = response.errors?.component1()?.message
        } else {
            _isLoading.value = false
            _toastMsg.value = response.data?.listSkillsRequired?.errors?.component1()
        }
        return listSkillReq
    }

    suspend fun jaccardSkills(idUser: Int, idLowongan: Int) {
        val listSkill = Optional.present(getSkills(idUser))
        val listRequired = Optional.present(getSkillsRequired(idLowongan))
        val response = apolloClient.query(JaccardSkillsQuery(listSkill, listRequired)).execute()
        if (response.data?.jaccardSkills?.success == true) {
            jaccard = response.data?.jaccardSkills?.jaccard ?: 0.0
        } else if (response.hasErrors()) {
            _toastMsg.value = response.errors?.component1()?.message
        } else {
            _toastMsg.value = response.data?.jaccardSkills?.errors?.component1()
        }
    }

    suspend fun createSkill(nama: String?) {
        val name = Optional.present(nama)
        try {
            _isLoading.value = true
            val response = apolloClient.mutation(CreateSkillsMutation(name)).execute()
            if (response.data?.createSkills?.success == true) {
                _isLoading.value = false
                _id.value = response.data?.createSkills?.skill?.id ?: 0
                _success.value = true
            } else if (response.hasErrors()) {
                _isLoading.value = false
                _success.value = false
                _toastMsg.value = response.errors?.component1()?.message
            } else {
                _isLoading.value = false
                _toastMsg.value = response.data?.createSkills?.errors?.component1()
            }
        } catch (e: ApolloException) {
            _isLoading.value = false
            _success.value = false
            _toastMsg.value = e.message.toString()
        }
    }

    suspend fun createLoker(
        name: String?,
        desc: String?,
        butuh: Int?,
        idCompany: Int?,
        photoUrl: String?
    ) {
        val nama = Optional.present(name)
        val deskripsi = Optional.present(desc)
        val jmlhButuh = Optional.present(butuh)
        val companyId = Optional.present(idCompany)
        val urlPhoto = Optional.present(photoUrl)
        try {
            _isLoading.value = true
            val response = apolloClient.mutation(
                CreateLowonganMutation(
                    nama,
                    deskripsi,
                    jmlhButuh,
                    companyId,
                    urlPhoto
                )
            ).execute()
            if (response.data?.createLowongan?.success == true) {
                _isLoading.value = false
                _success.value = true
                _id.value = response.data?.createLowongan?.lowongan?.id ?: 0
            } else {
                _success.value = false
                if (response.errors?.isNotEmpty() == true) {
                    _isLoading.value = false
                    _toastMsg.value = response.errors?.component1()?.message
                } else if (response.data?.createLowongan?.errors?.isNotEmpty() == true) {
                    _isLoading.value = false
                    _toastMsg.value = response.data?.createLowongan?.errors?.component1()
                }
            }
        } catch (e: ApolloException) {
            _isLoading.value = false
            _success.value = false
            _toastMsg.value = e.message.toString()
        }
    }

    suspend fun createSkillRequired(idLowongan: Int, idSkill: Int) {
        val lowonganId = Optional.present(idLowongan)
        val skillId = Optional.present(idSkill)
        try {
            _isLoading.value = true
            val response =
                apolloClient.mutation(CreateSkillRequiredMutation(skillId, lowonganId)).execute()
            if (response.hasErrors()) {
                _isLoading.value = false
                _success.value = false
                _toastMsg.value = response.errors?.component1()?.message
            } else if (response.data?.createSkillRequired?.errors?.isNotEmpty() == true) {
                _isLoading.value = false
                _success.value = false
                _toastMsg.value = response.data?.createSkillRequired?.errors?.component1()
            }
        } catch (e: ApolloException) {
            _isLoading.value = false
            _toastMsg.value = e.message.toString()
        }
    }

    suspend fun createPengalaman(
        nama_pekerjaan: String,
        tgl_mulai: String,
        tgl_selesai: String,
        tmpt_bekerja: String,
        pkrjn_profesional: Boolean,
        user_iduser: Int
    ) {
        val namaPekerjaan = Optional.present(nama_pekerjaan)
        val tglMulai = Optional.present(tgl_mulai)
        val tglSelesai = Optional.present(tgl_selesai)
        val tmptBekerja = Optional.present(tmpt_bekerja)
        val pkrjaanProfesional = Optional.present(pkrjn_profesional)
        val idUser = Optional.present(user_iduser)
        try {
            _isLoading.value = true
            val response = apolloClient.mutation(
                CreatePengalamanMutation(
                    namaPekerjaan,
                    tglMulai,
                    tglSelesai,
                    tmptBekerja,
                    pkrjaanProfesional,
                    idUser
                )
            ).execute()
            if (response.data?.createPengalaman?.success == true) {
                _isLoading.value = false
                _success.value = true
            } else {
                _success.value = false
                if (response.errors?.isNotEmpty() == true) {
                    _isLoading.value = false
                    _toastMsg.value = response.errors?.component1()?.message
                } else {
                    _isLoading.value = false
                    _toastMsg.value = response.data?.createPengalaman?.errors?.component1()
                }
            }
        } catch (e: ApolloException) {
            _isLoading.value = false
            _success.value = false
            _toastMsg.value = e.message.toString()
        }
    }

    suspend fun createEdukasi(
        user_iduser: Int,
        nama_institusi: String,
        deskripsi: String,
        jenjang: String,
        tgl_awal: String,
        tgl_akhir: String
    ) {
        val idUser = Optional.present(user_iduser)
        val nama = Optional.present(nama_institusi)
        val desc = Optional.present(deskripsi)
        val jenjang = Optional.present(jenjang)
        val tglAwal = Optional.present(tgl_awal)
        val tglAkhir = Optional.present(tgl_akhir)

        try {
            _isLoading.value = true
            val response = apolloClient.mutation(
                CreateEdukasiMutation(
                    idUser,
                    nama,
                    desc,
                    jenjang,
                    tglAwal,
                    tglAkhir
                )
            ).execute()
            if (response.data?.createEdukasi?.success == true) {
                _isLoading.value = false
                _success.value = true
            } else {
                if (response.errors?.isNotEmpty() == true) {
                    _isLoading.value = false
                    _toastMsg.value = response.errors?.component1()?.message
                } else {
                    _isLoading.value = false
                    _toastMsg.value = response.data?.createEdukasi?.errors?.component1()
                }
            }
        } catch (e: ApolloException) {
            _isLoading.value = false
            _success.value = false
            _toastMsg.value = e.message.toString()
        }
    }

    suspend fun updateUser(
        userId: Int?,
        pengalaman: Int?,
        pengalamanPro: Int?
    ) {
        val idUser = Optional.present(userId)
        val pengalaman = Optional.present(pengalaman)
        val pengalamanPro = Optional.present(pengalamanPro)
        val response =
            apolloClient.mutation(UpdateUserMutation(idUser, pengalaman, pengalamanPro)).execute()
        try {
            if (response.data?.updateUser?.success == true) {
                _success.value = true
            } else {
                _success.value = false
                if (response.errors?.isNotEmpty() == true) {
                    _toastMsg.value = response.errors?.component1()?.message
                } else {
                    _toastMsg.value = response.data?.updateUser?.errors?.component1()
                }
            }
        } catch (e: ApolloException) {
            _toastMsg.value = e.message.toString()
            _success.value = false
        }
    }

    suspend fun updateEdukasi(id: Int, edu: String) {
        val idUser = Optional.present(id)
        val education = Optional.present(edu)
        try {
            val response =
                apolloClient.mutation(UpdateEducationMutation(idUser, education)).execute()
            if (response.data?.updateEducation?.success == true) {
                _success.value = true
            } else if (response.errors?.isNotEmpty() == true) {
                _success.value = false
                _toastMsg.value = response.errors?.component1()?.message
            } else {
                _success.value = false
//                _toastMsg.value = response.data?.updateEducation?.errors?.component1()
            }
        } catch (e: ApolloException) {
            _success.value = false
            _toastMsg.value = e.message.toString()
        }
    }

//    @OptIn(DelicateCoroutinesApi::class)
//    fun saveProfile(
//        context: Context,
//        imageUri: Uri,
//        onSuccess: (String) -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        val imageFileName = "images/${UUID.randomUUID()}.jpg"
//        val blobInfo = BlobInfo.newBuilder(gcsBucketName, imageFileName).build()
//
//        GlobalScope.launch(Dispatchers.IO) {
//            try {
//                context.contentResolver.openInputStream(imageUri).use { imageStream ->
//                    if (imageStream != null) {
//                        val imageBytes = ByteArrayOutputStream().use { output ->
//                            imageStream.copyTo(output)
//                            output.toByteArray()
//                        }
//                        val imageBlob = storage.create(blobInfo, imageBytes)
//                        val imageUrl = imageBlob.mediaLink
//
//                        onSuccess(imageUrl)
//                    } else {
//                        onFailure(Exception("Failed to open image stream"))
//                    }
//                }
//            } catch (e: Exception) {
//                onFailure(e)
//            }
//        }
//    }


    fun saveProfile(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val storageReference = FirebaseStorage.getInstance().reference
        val imageFileName = "images/${UUID.randomUUID()}.jpg"
        val imageRef = storageReference.child(imageFileName)
        val uploadTask = imageRef.putFile(imageUri)

        _isLoading.value = true
        uploadTask.addOnSuccessListener {
            _isLoading.value = false
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
            _isLoading.value = false
        }
    }

    suspend fun updateDatabase(
        idUser: Int,
        description: String,
        imageUrl: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = Optional.present(idUser)
        val desc = Optional.present(description)
        val imgUrl = Optional.present(imageUrl)
        try {
            _isLoading.value = true
            val updateUrlPhoto =
                apolloClient.mutation(UpdateUserUrlPhotoMutation(userId, imgUrl)).execute()
            val updateDesc =
                apolloClient.mutation(UpdateUserDescriptionMutation(userId, desc)).execute()
            if (updateDesc.data?.updateUserDescription?.success == true && updateUrlPhoto.data?.updateUserUrlPhoto?.success == true) {
                _isLoading.value = false
                onSuccess()
            } else if (updateDesc.hasErrors() || updateUrlPhoto.hasErrors()) {
                _isLoading.value = false
                _toastMsg.value = updateUrlPhoto.errors?.component1()?.message
            } else {
                _isLoading.value = false
                _toastMsg.value = updateUrlPhoto.data?.updateUserUrlPhoto?.errors?.component1()
            }
        } catch (e: ApolloException) {
            _isLoading.value = false
            onFailure(e)
        }
    }

    suspend fun updateDatabaseCompany(
        idCompany: Int,
        imageUrl: String,
        deskripsi: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val companyId = Optional.present(idCompany)
        val photoUrl = Optional.present(imageUrl)
        val desc = Optional.present(deskripsi)
        try {
            _isLoading.value = true
            val response =
                apolloClient.mutation(UpdateCompanyUrlPhotoMutation(companyId, photoUrl)).execute()
            val response2 =
                apolloClient.mutation(UpdateCompanyDescriptionMutation(companyId, desc)).execute()
            if (response.data?.updateCompanyUrlPhoto?.success == true && response2.data?.updateCompanyDescription?.success == true) {
                _isLoading.value = false
                onSuccess()
            } else if (response.hasErrors() || response2.hasErrors()) {
                _isLoading.value = false
                _toastMsg.value = response.errors?.component1()?.message
            } else {
                _isLoading.value = false
                _toastMsg.value = response.data?.updateCompanyUrlPhoto?.errors?.component1()
            }
        } catch (e: ApolloException) {
            _isLoading.value = false
            onFailure(e)
        }
    }

    fun getUser(): Flow<UserModel> = pref.getUser()
    suspend fun logout() = pref.logout()
    suspend fun saveUser(user: UserModel) = pref.saveUser(user)
}
