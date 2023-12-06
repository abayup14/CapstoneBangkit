package com.haire.data

import com.haire.R

object InitialDummyValue {
    val dummyJobs = listOf(
        Jobs(R.drawable.logo, "Kotabaru, South Kalimantan", "MINING DATA ENGINEER & ANALYST", "Kalimantan"),
        Jobs(R.drawable.jobs1, "Jakarta", "Cyber Security", "Jakarta"),
        Jobs(R.drawable.jobs2, "Kotabaru, South Kalimantan", "Front End Developer", "Kalimantan"),
        Jobs(R.drawable.jobs3, "Surabaya, East Java, Indonesia", "AI-Augmented Software Developer", "Surabaya")
    )

    val dummyStatus = listOf(
        Status(dummyJobs[3], "Accepted"),
        Status(dummyJobs[2], "Rejected"),
        Status(dummyJobs[1], "Pending")
    )
}