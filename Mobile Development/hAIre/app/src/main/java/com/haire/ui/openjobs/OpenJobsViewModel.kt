package com.haire.ui.openjobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.haire.data.InitialDummyValue
import com.haire.data.Jobs

class OpenJobsViewModel : ViewModel() {
    private val _jobs = MutableLiveData<List<Jobs>>()
    val jobs: LiveData<List<Jobs>> = _jobs

    init {
        val dummyData = InitialDummyValue.dummyJobs
        _jobs.value = dummyData
    }
}