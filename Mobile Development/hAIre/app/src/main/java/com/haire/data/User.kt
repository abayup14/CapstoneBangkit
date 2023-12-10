package com.haire.data

data class User (
    val name: String,
    val phone: String,
    val email: String,
    val homeless: Boolean,
    val disabled: Boolean,
    val photoUrl: String = "",
    val about: String = "",
    val edukasi: EdukasiEnum = EdukasiEnum.UNDERGRADUATE,
    val skill: List<String> = listOf(""),
    val experience: Int = 0
)