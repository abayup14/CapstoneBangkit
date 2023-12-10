package com.haire.di

import android.content.Context
import com.haire.JobRepository
import com.haire.data.UserPreference
import com.haire.data.dataStore

object Injection {
    fun provideRepository(context: Context): JobRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return JobRepository(pref)
    }
}