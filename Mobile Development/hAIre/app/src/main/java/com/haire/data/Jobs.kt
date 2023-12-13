package com.haire.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Jobs(
    val company: Company,
    val pekerjaan: String,
    val deskripsi: String = "",
    val jmlButuh: Int = 0
) : Parcelable
