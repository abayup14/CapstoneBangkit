package com.haire

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.haire.data.Company
import com.haire.data.EdukasiEnum
import com.haire.data.Jobs
import com.haire.data.User
import com.haire.data.UserModel
import com.haire.data.UserPreference
import kotlinx.coroutines.flow.Flow

class JobRepository(private val pref: UserPreference) {
    private val dbUser: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private val dbJobs: DatabaseReference = FirebaseDatabase.getInstance().getReference("jobs")
    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> = _toastMsg

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _isCompany = MutableLiveData<Boolean>()
    val isCompany: LiveData<Boolean> = _isCompany

    private val _profileAccount = MutableLiveData<User>()
    val profileAccount: LiveData<User> = _profileAccount

    private val _jobVacancy = MutableLiveData<List<Jobs>>()
    val jobVacancy: LiveData<List<Jobs>> = _jobVacancy

    private val _companyData = MutableLiveData<Company>()
    val companyData: LiveData<Company> = _companyData

    fun deleteAccount(email: String) {
        val currentUser = FirebaseDatabase.getInstance().getReference("users/$email")
        currentUser.removeValue()
    }

    fun loginAccount(context: Context, email: String, password: String) {
        dbUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(email).exists()) {
                    val isCompany =
                        snapshot.child(email).child("company").getValue(Boolean::class.java)
                    if (snapshot.child(email).child("password").getValue(String::class.java)
                            .equals(password)
                    ) {
                        if (isCompany == true) {
                            _isCompany.value = true
                            _success.value = false
                        } else {
                            _isCompany.value = false
                            _success.value = true
                        }
                    } else {
                        _success.value = false
                        _toastMsg.value = context.getString(R.string.password_not_correct)
                    }
                } else {
                    _success.value = false
                    _toastMsg.value = context.getString(R.string.email_not_registered)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _success.value = false
                _toastMsg.value = error.message
            }
        })
    }

    fun registerAccount(user: User, pass: String) {
        dbUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val emailExist = snapshot.child(user.email).exists()
                _success.value = emailExist
                if (!emailExist) {
                    dbUser.child(user.email).apply {
                        child("name").setValue(user.name)
                        child("email").setValue(user.email)
                        child("phone").setValue(user.phone)
                        child("password").setValue(pass)
                        child("isHomeless").setValue(user.homeless)
                        child("isDisabled").setValue(user.disabled)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun registerCompany(company: Company, pass: String) {
        dbUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val emailExist = snapshot.child(company.email).exists()
                _success.value = emailExist
                if (!emailExist) {
                    dbUser.child(company.email).apply {
                        child("name").setValue(company.name)
                        child("email").setValue(company.email)
                        child("address").setValue(company.address)
                        child("password").setValue(pass)
                        child("company").setValue(true)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun addJob(jobs: Jobs) {

    }

    fun getJobVacancy(email: String) {
        dbJobs.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jobsList = mutableListOf<Jobs>()
                val emailSnapshot = snapshot.child(email)
                for (a in emailSnapshot.children) {
                    val pekerjaan = a.child("pekerjaan").getValue(String::class.java) ?: ""
                    val alamat = a.child("alamat").getValue(String::class.java) ?: ""
                    val photoUrl = a.child("photoUrl").getValue(String::class.java) ?: ""
                    val deskripsiCompany =
                        a.child("deskripsiCompany").getValue(String::class.java) ?: ""
                    val deskripsi = a.child("deskripsi").getValue(String::class.java) ?: ""
                    val jmlButuh = a.child("jml_butuh").getValue(Int::class.java) ?: 0

                    val job = Jobs(
                        Company(
                            _profileAccount.value?.name ?: "",
                            alamat,
                            email,
                            photoUrl,
                            deskripsiCompany
                        ), pekerjaan, deskripsi, jmlButuh
                    )
                    jobsList.add(job)
                }
                _jobVacancy.value = jobsList
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getCompanyData(email: String) {
        val name = _profileAccount.value?.name ?: ""
        dbUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val emailSnapshot = snapshot.child(email)
                val alamat = emailSnapshot.child("alamat").getValue(String::class.java) ?: ""
                val photoUrl = emailSnapshot.child("photoUrl").getValue(String::class.java) ?: ""
                val deskripsiCompany =
                    emailSnapshot.child("deskripsiCompany").getValue(String::class.java) ?: ""

                val company = Company(
                    name,
                    alamat,
                    email,
                    photoUrl,
                    deskripsiCompany
                )
                _companyData.value = company
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getProfileAccount(email: String) {
        dbUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child(email).child("name").getValue(String::class.java) ?: ""
                val phone = snapshot.child(email).child("phone").getValue(String::class.java) ?: ""
                val photoUrl =
                    snapshot.child(email).child(("photo_url")).getValue(String::class.java) ?: ""
                val homeless =
                    snapshot.child(email).child("homeless").getValue(Boolean::class.java) ?: false
                val disabled =
                    snapshot.child(email).child("disabled").getValue(Boolean::class.java) ?: false
                val edukasi =
                    snapshot.child(email).child("edukasi").getValue(EdukasiEnum::class.java)
                        ?: EdukasiEnum.UNDERGRADUATE
                val pengalaman =
                    snapshot.child(email).child("pengalaman").getValue(Int::class.java) ?: 0
                val about =
                    snapshot.child(email).child("deskripsi").getValue(String::class.java) ?: ""
                val user = User(
                    name,
                    phone,
                    email,
                    homeless,
                    disabled,
                    photoUrl,
                    about,
                    edukasi,
                    listOf(),
                    pengalaman
                )
                _profileAccount.value = user
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getUser(): Flow<UserModel> = pref.getUser()
    suspend fun logout() = pref.logout()
    suspend fun saveUser(user: UserModel) = pref.saveUser(user)
}