package com.haire.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Company(
    val name: String,
    val address: String,
    val email: String,
    val photoUrl: String = "",
    val description: String = ""
) : Parcelable
