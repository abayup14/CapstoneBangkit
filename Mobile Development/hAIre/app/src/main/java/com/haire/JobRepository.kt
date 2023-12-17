package com.haire

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.google.firebase.storage.FirebaseStorage
import com.haire.data.Jobs
import com.haire.data.UserModel
import com.haire.data.UserPreference
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class JobRepository(private val pref: UserPreference) {
    private val _isCompany = MutableLiveData<Boolean>()
    val isCompany: LiveData<Boolean> = _isCompany

    private val _id = MutableLiveData<Int>()
    val id: LiveData<Int> = _id

    private val _skill = MutableLiveData<List<ListUserSkillsQuery.Skill?>>()
    val skill: LiveData<List<ListUserSkillsQuery.Skill?>> = _skill

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

    private val apolloClient = ApolloClient.Builder()
        .serverUrl("https://graphqlapi-vdnbldljhq-et.a.run.app/graphql")
        .build()

    suspend fun loginAccount(email: String, password: String) {
        val email = Optional.present(email ?: "")
        val pass = Optional.present(password ?: "")

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
                _success.value = false
                _isCompany.value = true
                _id.value = response2.data?.cekLoginCompany?.company?.id ?: 0
            } else {
                if (response.hasErrors()) {
                    _toastMsg.value = "There's an error while connecting to server."
                } else {
                    if (response.data?.cekLoginUser?.success == true) {
                        _isCompany.value = false
                        _success.value = true
                        _id.value = response.data?.cekLoginUser?.user?.iduser ?: 0
                    } else {
                        _toastMsg.value = response.data?.cekLoginUser?.errors?.component1()
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
                _success.value = true
            } else {
                _success.value = false
                _toastMsg.value = response.data?.createUser?.errors?.component1()
            }
        } catch (e: ApolloException) {
            Log.w("Register", "Failed to Register", e)
            _success.value = false
            _toastMsg.value = e.message.toString()
        }
    }

    suspend fun registerCompany(nama: String, alamat: String, email: String, password: String) {
        val nama = Optional.present(nama ?: "")
        val alamat = Optional.present(alamat ?: "")
        val mail = Optional.present(email ?: "")
        val pass = Optional.present(password ?: "")

        try {
            val response =
                apolloClient.mutation(CreateCompanyMutation(nama, alamat, mail, pass)).execute()
            if (response.data?.createCompany?.success == true) {
                _success.value = true
            } else {
                _success.value = false
                _toastMsg.value = response.data?.createCompany?.errors?.component1()
            }
        } catch (e: ApolloException) {
            Log.w("Register", "Failed to Register", e)
            _success.value = false
            _toastMsg.value = e.message.toString()
        }
    }

    suspend fun getListLoker() {
        val response = apolloClient.query(ListLowongansQuery()).execute()
        if (response.data?.listLowonganUserSearch?.success == true) {
            _loker.value = response.data?.listLowonganUserSearch?.lowongan ?: emptyList()
        } else {
            _toastMsg.value = response.data?.listLowonganUserSearch?.errors?.component1()
        }
    }

    suspend fun getLokerCompany(companyId: Int) {
        val idCompany = Optional.present(companyId)
        val response = apolloClient.query(ListLowonganCompanyQuery(idCompany)).execute()
        if (response.data?.listLowonganCompany?.success == true) {
            _lokerCompany.value = response.data?.listLowonganCompany?.lowongan ?: emptyList()
        } else {
            _toastMsg.value = response.data?.listLowonganCompany?.errors?.component1()
        }
    }

    suspend fun getProfileDataCompany(idUser: Int?) {
        val userId = Optional.present(idUser)
        val response = apolloClient.query(ProfileCompanyQuery(userId)).execute()
        if (response.data?.profileCompany?.success == true) {
            _profileCompanyData.value = response.data?.profileCompany?.company!!
        } else {
            _toastMsg.value = response.data?.profileCompany?.errors?.component1()
        }
    }

    suspend fun getProfileData(idUser: Int?) {
        val userId = Optional.present(idUser)
        val response = apolloClient.query(ProfileUserQuery(userId)).execute()
        if (response.data?.profileUser?.success == true) {
            _profileData.value = response.data?.profileUser?.user!!
        } else {
            _toastMsg.value = response.data?.profileUser?.errors?.component1()
        }
    }

    suspend fun getPengalaman(id: Int?) {
        val userId = Optional.present(id)
        val response = apolloClient.query(ListPengalamanQuery(userId)).execute()
        if (response.data?.listPengalamanUser?.success == true) {
            _exp.value = response.data?.listPengalamanUser?.pengalaman ?: emptyList()
        } else {
            if (response.data?.listPengalamanUser?.errors?.isNotEmpty() == true) {
                _toastMsg.value = response.data?.listPengalamanUser?.errors?.component1()
            }
        }
    }

    suspend fun getEdukasi(id: Int?) {
        val idUser = Optional.present(id)
        val response = apolloClient.query(ListEdukasiQuery(idUser)).execute()
        if (response.data?.listEdukasiUser?.success == true) {
            _edu.value = response.data?.listEdukasiUser?.edukasi ?: emptyList()
        } else {
            if (response.data?.listEdukasiUser?.errors?.isNotEmpty() == true) {
                _toastMsg.value = response.data?.listEdukasiUser?.errors?.component1()
            }
        }
    }

    suspend fun getSkills(id: Int?) {
        val idUser = Optional.present(id)
        val response = apolloClient.query(ListUserSkillsQuery(idUser)).execute()
        if (response.data?.listUserSkills?.success == true) {
            _skill.value = response.data?.listUserSkills?.skills ?: emptyList()
        } else {
            if (response.data?.listUserSkills?.errors?.isNotEmpty() == true) {
                _toastMsg.value = response.data?.listUserSkills?.errors?.component1()
            }
        }
    }

    suspend fun createSkill(nama: String?) {
        val name = Optional.present(nama)
        try {
            val response = apolloClient.mutation(CreateSkillsMutation(name)).execute()
            if (response.data?.createSkills?.success == true) {
                _success.value = true
            } else {
                _success.value = false
                if (response.data?.createSkills?.errors?.isNotEmpty() == true) {
                    _toastMsg.value = response.data?.createSkills?.errors?.component1()
                }
            }
        } catch (e: ApolloException) {
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
            val response = apolloClient.mutation(CreateLowonganMutation(nama, deskripsi, jmlhButuh, companyId, urlPhoto)).execute()
            if (response.data?.createLowongan?.success == true) {
                _success.value = true
            } else {
                _success.value = false
                if (response.data?.createLowongan?.errors?.isNotEmpty() == true) {
                    _toastMsg.value = response.data?.createLowongan?.errors?.component1()
                }
            }
        } catch (e: ApolloException) {
            _success.value = false
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
            val response = apolloClient.mutation(CreatePengalamanMutation(namaPekerjaan, tglMulai, tglSelesai, tmptBekerja, pkrjaanProfesional, idUser)).execute()
            if (response.data?.createPengalaman?.success == true) {
                _success.value = true
            } else {
                _success.value = false
                if (response.data?.createPengalaman?.errors?.isNotEmpty() == true) {
                    _toastMsg.value = response.data?.createPengalaman?.errors?.component1()
                }
                _toastMsg.value = "gagal"
            }
        } catch (e: ApolloException) {
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
            val response = apolloClient.mutation(CreateEdukasiMutation(idUser, nama, desc, jenjang, tglAwal, tglAkhir)).execute()
            if (response.data?.createEdukasi?.success == true) {
                _success.value = true
            } else {
                if (response.errors?.isNotEmpty() == true) {
                    _toastMsg.value = response.errors?.component1()?.message
                } else {
                    _toastMsg.value = response.data?.createEdukasi?.errors?.component1()
                }
            }
        } catch (e: ApolloException) {
            _success.value = false
            _toastMsg.value = e.message.toString()
        }
    }

    suspend fun updateUser(
        idUser: Int?,
        pengalaman: Int?,
        pengalamanPro: Int?
    ) {
        val idUser = Optional.present(idUser)
        val pengalaman = Optional.present(pengalaman)
        val pengalamanPro = Optional.present(pengalamanPro)
        val response = apolloClient.mutation(UpdateUserMutation(idUser, pengalaman, pengalamanPro)).execute()
        try {
            if (response.data?.updateUser?.success == true)  {
                _success.value = true
            } else {
                _success.value = false
                if (response.data?.updateUser?.errors?.isNotEmpty() == true) {
                    _toastMsg.value = response.data?.updateUser?.errors?.component1()
                }
            }
        } catch (e: ApolloException) {
            _toastMsg.value = e.message.toString()
            _success.value = false
        }
    }

    fun saveProfile(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val storageReference = FirebaseStorage.getInstance().reference
        val imageFileName = "images/${UUID.randomUUID()}.jpg"
        val imageRef = storageReference.child(imageFileName)
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }

//    fun updateDatabase(
//        email: String,
//        description: String,
//        age: Int,
//        imageUrl: String,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        val imagesReference = dbUser.child(email).child("photo_url")
//        imagesReference.setValue(imageUrl)
//            .addOnSuccessListener {
//                onSuccess()
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//        dbUser.child(email).child("deskripsi").setValue(description)
//        dbUser.child(email).child("umur").setValue(age)
//    }

//    fun updateDatabaseCompany(
//        email: String,
//        description: String,
//        imageUrl: String,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        val imagesReference = dbUser.child(email).child("photoUrl")
//        imagesReference.setValue(imageUrl)
//            .addOnSuccessListener {
//                onSuccess()
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//        dbUser.child(email).child("company_desc").setValue(description)
//    }

    fun getUser(): Flow<UserModel> = pref.getUser()
    suspend fun logout() = pref.logout()
    suspend fun saveUser(user: UserModel) = pref.saveUser(user)
}
