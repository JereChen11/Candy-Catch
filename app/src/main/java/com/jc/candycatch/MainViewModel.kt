package com.jc.candycatch

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val TAG = "MainViewModel"
    val pointViewLiveData = MutableLiveData<Int>()

    fun generatePointViewOnTime() {

        viewModelScope.launch {
            for (i in 0..50) {
                Log.e(TAG, "generatePointViewOnTime: i = $i")
                pointViewLiveData.value = i
                delay(500)
            }
        }

    }
}