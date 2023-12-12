package com.haire

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haire.di.Injection
import com.haire.ui.company.CompanyViewModel
import com.haire.ui.detail.DetailViewModel
import com.haire.ui.home.HomeViewModel
import com.haire.ui.login.LoginViewModel
import com.haire.ui.openjobs.OpenJobsViewModel
import com.haire.ui.profile.ProfileViewModel
import com.haire.ui.register.RegisterViewModel
import com.haire.ui.status.StatusViewModel

class ViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel() as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel() as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(OpenJobsViewModel::class.java) -> {
                OpenJobsViewModel() as T
            }

            modelClass.isAssignableFrom(StatusViewModel::class.java) -> {
                StatusViewModel() as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(CompanyViewModel::class.java) -> {
                CompanyViewModel(Injection.provideRepository(context)) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}