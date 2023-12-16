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

    var id: Int = 0

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> = _toastMsg

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _loker = MutableLiveData<List<ListLowongansQuery.ListLowongan?>>()
    val loker: LiveData<List<ListLowongansQuery.ListLowongan?>> = _loker

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
                id = response2.data?.cekLoginCompany?.company?.id ?: 0
            } else {
                if (response.hasErrors()) {
                    _toastMsg.value = "There's an error while connecting to server."
                } else {
                    if (response.data?.cekLoginUser?.success == true) {
                        _isCompany.value = false
                        _success.value = true
                        id = response.data?.cekLoginUser?.user?.iduser ?: 0
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
        nik: String?
    ) {
        val name: Optional<String> = Optional.present(nama ?: "")
        val mail: Optional<String> = Optional.present(email ?: "")
        val pass: Optional<String> = Optional.present(password ?: "")
        val hp: Optional<String> = Optional.present(nomor ?: "")
        val lahir: Optional<String> = Optional.present(tgl ?: "")
        val nikk: Optional<String> = Optional.present(nik ?: "")

        try {
            val response = apolloClient.mutation(
                CreateUserMutation(
                    nama = name,
                    email = mail,
                    password = pass,
                    nomor = hp,
                    tgl = lahir,
                    nik = nikk
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
        _loker.value = response.data?.listLowongans ?: emptyList()
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
                _toastMsg.value = response.data?.createLowongan?.errors?.component1()
            }
        } catch (e: ApolloException) {
            _success.value = false
            _toastMsg.value = e.message.toString()
        }
    }

    fun addJob(jobs: Jobs) {

    }

    fun getJobByCompany(email: String) {

    }

    fun getProfileAccount(email: String) {

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
