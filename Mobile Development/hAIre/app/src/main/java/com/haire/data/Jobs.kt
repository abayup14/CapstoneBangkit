package com.haire.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Jobs(
    val image: Int,
    val alamat: String,
    val pekerjaan: String,
    val provinsi: String
) : Parcelable
