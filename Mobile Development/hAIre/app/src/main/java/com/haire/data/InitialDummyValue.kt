package com.haire.data

import com.haire.R

object InitialDummyValue {
    val dummyJobs = listOf(
        Jobs(Company("hAIre", "Surabaya", "haire@haire.com", R.drawable.logo.toString()), "MINING DATA ENGINEER & ANALYST"),
        Jobs(Company("hAIre", "Surabaya", "haire@haire.com", R.drawable.jobs1.toString()), "Cyber Security"),
        Jobs(Company("hAIre", "Surabaya", "haire@haire.com", R.drawable.logo.toString()), "Front End Developer"),
        Jobs(Company("hAIre", "Surabaya", "haire@haire.com", R.drawable.logo.toString()), "AI-Augmented Software Developer")
    )

    val dummyStatus = listOf(
        Status(dummyJobs[3], "Accepted"),
        Status(dummyJobs[2], "Rejected"),
        Status(dummyJobs[1], "Pending")
    )

    val dummyVacancy = listOf(
        Jobs(Company("hAIre", "Surabaya", "haire@haire.com", R.drawable.logo.toString()),"MINING DATA ENGINEER & ANALYST"),
        Jobs(Company("hAIre", "Surabaya", "haire@haire.com", R.drawable.logo.toString()),"Cyber Security"),
        Jobs(Company("hAIre", "Surabaya", "haire@haire.com", R.drawable.logo.toString()),"Android Developer"),
        Jobs(Company("hAIre", "Surabaya", "haire@haire.com", R.drawable.logo.toString()),"Front End Developer")
    )
}