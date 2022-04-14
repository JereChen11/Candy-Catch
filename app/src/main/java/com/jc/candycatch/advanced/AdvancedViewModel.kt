package com.jc.candycatch.advanced

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdvancedViewModel : ViewModel() {
    val TAG = "AdvancedViewModel"
    private val _pointViewMld = MutableLiveData<Int>()
    val pointViewLd: LiveData<Int> = _pointViewMld

    private val delayTime = 18000L

    fun generatePointViewOnTime() {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 41..100) {
                Log.e(TAG, "generatePointViewOnTime: i = $i")
                _pointViewMld.postValue(i)
                delay(delayTime / i)
            }
        }

    }
}