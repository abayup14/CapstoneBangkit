package com.haire.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.haire.data.InitialDummyValue
import com.haire.data.Status

class StatusViewModel : ViewModel() {

    private val _status = MutableLiveData<List<Status>>()
    val status: LiveData<List<Status>> = _status

    init {
        val dummyStatus = InitialDummyValue.dummyStatus
        _status.value = dummyStatus
    }
}