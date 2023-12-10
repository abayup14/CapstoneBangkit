package com.haire

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.haire.data.EdukasiEnum
import com.haire.data.Jobs
import com.haire.data.User
import com.haire.data.UserModel
import com.haire.data.UserPreference
import kotlinx.coroutines.flow.Flow

class JobRepository(private val pref: UserPreference) {
    private val dbUser: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> = _toastMsg

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _profileAccount = MutableLiveData<User>()
    val profileAccount: LiveData<User> = _profileAccount

    fun loginAccount(context: Context, email: String, password: String) {
        dbUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(email).exists()) {
                    if (snapshot.child(email).child("password").getValue(String::class.java)
                            .equals(password)
                    ) {
                        _success.value = true
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

    fun registerAccount(user: User, pass: String){
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

    fun getProfileAccount(email: String){
        dbUser.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child(email).child("name").getValue(String::class.java) ?: ""
                val phone = snapshot.child(email).child("phone").getValue(String::class.java) ?: ""
                val photoUrl = snapshot.child(email).child(("photo_url")).getValue(String::class.java) ?: ""
                val edukasi = snapshot.child(email).child("edukasi").getValue(EdukasiEnum::class.java) ?: EdukasiEnum.UNDERGRADUATE
                val pengalaman = snapshot.child(email).child("pengalaman").getValue(Int::class.java) ?: 0
                val about = snapshot.child(email).child("deskripsi").getValue(String::class.java) ?: ""
                val user = User(name, phone, email, false, false, photoUrl, about, edukasi, listOf(), pengalaman)
                _profileAccount.value = user
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
    fun getUser(): Flow<UserModel> = pref.getUser()
    suspend fun logout() = pref.logout()
    suspend fun saveUser(user: UserModel) = pref.saveUser(user)
}