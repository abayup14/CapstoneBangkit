package com.haire

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.google.firebase.storage.FirebaseStorage
import com.haire.data.Company
import com.haire.data.Jobs
import com.haire.data.User
import com.haire.data.UserModel
import com.haire.data.UserPreference
import com.haire.network.ApiClient
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class JobRepository(private val pref: UserPreference) {
//    private val dbUser: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private val apiClient: ApolloClient = ApiClient.get()

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> = _toastMsg

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _profileAccount = MutableLiveData<User>()
    val profileAccount: LiveData<User> = _profileAccount

    private val _jobVacancy = MutableLiveData<List<Jobs>>()
    val jobVacancy: LiveData<List<Jobs>> = _jobVacancy

    private val _companyData = MutableLiveData<Company>()
    val companyData: LiveData<Company> = _companyData

    val apolloClient = ApolloClient.Builder()
        .serverUrl("https://graphqlapi-vdnbldljhq-et.a.run.app/graphql")
        .build()

    fun deleteAccount(email: String) {

    }

    suspend fun loginAccount(email: String, password: String) {
        val email = Optional.present(email ?: "")
        val pass = Optional.present(password ?: "")

        try{
            apolloClient.query(CekLoginUserQuery(email, pass)).execute()
            _success.value = true
        } catch (e: ApolloException) {
            Log.w("Login", "Failed to Login", e)
            _success.value = false
        }

//        val response = apiClient.query()

        // Firebase
//        dbUser.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.child(email).exists()) {
//                    val isCompany =
//                        snapshot.child(email).child("company").getValue(Boolean::class.java)
//                    if (snapshot.child(email).child("password").getValue(String::class.java)
//                            .equals(password)
//                    ) {
//                        if (isCompany == true) {
//                            _isCompany.value = true
//                            _success.value = false
//                        } else {
//                            _isCompany.value = false
//                            _success.value = true
//                        }
//                    } else {
//                        _success.value = false
//                        _toastMsg.value = context.getString(R.string.password_not_correct)
//                    }
//                } else {
//                    _success.value = false
//                    _toastMsg.value = context.getString(R.string.email_not_registered)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                _success.value = false
//                _toastMsg.value = error.message
//            }
//        })
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

        try{
            apolloClient.mutation(CreateUserMutation(nama = name, email = mail, password = pass, nomor = hp, tgl = lahir, nik = nikk)).execute()
            _success.value = true
        } catch (e: ApolloException) {
            Log.w("Register", "Failed to Register", e)
            _success.value = false
        }

        // Firebase
//        dbUser.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val emailExist = snapshot.child(user.email).exists()
//                _success.value = emailExist
//                if (!emailExist) {
//                    dbUser.child(user.email).apply {
//                        child("name").setValue(user.name)
//                        child("email").setValue(user.email)
//                        child("phone").setValue(user.phone)
//                        child("password").setValue(pass)
//                        child("isHomeless").setValue(user.homeless)
//                        child("isDisabled").setValue(user.disabled)
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//        })
    }

    suspend fun registerCompany(nama: String, alamat: String, email: String, password: String) {
        val nama = Optional.present(nama ?: "")
        val alamat = Optional.present(alamat ?: "")
        val mail = Optional.present(email ?: "")
        val pass = Optional.present(password ?: "")

        try{
            apolloClient.mutation(CreateCompanyMutation(nama, alamat, mail, pass)).execute()
            _success.value = true
        } catch (e: ApolloException) {
            Log.w("Register", "Failed to Register", e)
            _success.value = false
        }
//        Firebase
//        dbUser.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val emailExist = snapshot.child(company.email).exists()
//                _success.value = emailExist
//                if (!emailExist) {
//                    dbUser.child(company.email).apply {
//                        child("name").setValue(company.name)
//                        child("email").setValue(company.email)
//                        child("address").setValue(company.address)
//                        child("password").setValue(pass)
//                        child("company").setValue(true)
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//        })

    }

    fun addJob(jobs: Jobs) {

    }

    fun getJobVacancy(email: String) {

    }

    fun getCompanyData(email: String) {

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
