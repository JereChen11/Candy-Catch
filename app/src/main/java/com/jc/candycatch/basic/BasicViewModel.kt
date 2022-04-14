package com.jc.candycatch.basic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BasicViewModel : ViewModel() {
    val TAG = "BasicViewModel"
    private val _pointViewMld = MutableLiveData<Int>()
    val pointViewLd: LiveData<Int> = _pointViewMld
    val countDownTimeLiveData = MutableLiveData<Int>()

    private val delayTime = 18000L

    fun generatePointViewOnTime() {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 41..100) {
                Log.e(TAG, "generatePointViewOnTime: i = $i")
                _pointViewMld.postValue(i)
//                if (i % 4 == 0) {
//                    countDownTimeLiveData.postValue(i / 4)
//                }
                //1s 四个点
                //todo add 自定义加速度生成。那我控制delay的变量不就可以了。
                //i 越大，delay 越小。
//                delay(250)
                //todo 难度的控制就是控制这个变量
                delay(delayTime / i)
            }
        }

    }
}