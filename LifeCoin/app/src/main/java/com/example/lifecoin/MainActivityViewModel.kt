package com.example.lifecoin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    var steps = MutableLiveData<Int> ()
    var lifecoin = MutableLiveData<Float> ()
    var lastPaidSteps = MutableLiveData<Float> ()
    var scans = MutableLiveData<Int> ()
    var scanCode = MutableLiveData<Boolean>()

    init {
        steps.value = 0
        lifecoin.value = 0f
        lastPaidSteps.value = 0f
        scans.value = 0
        scanCode.value = false
    }

}