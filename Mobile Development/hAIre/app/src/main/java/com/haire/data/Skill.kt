package com.haire.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Skill(
    val id: Int,
    val nama: String
) : Parcelable