package com.jc.candycatch

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import androidx.lifecycle.ViewModelProvider
import com.jc.candycatch.databinding.ActivityMainBinding
import com.jc.candycatch.utils.px2dp

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val pointViewMap = mutableMapOf<Int, PointView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        startMoving(binding.pointView)

        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.pointViewLiveData.observe(this, {
            Log.e(TAG, "onCreate: it = $it")

            //todo generate a View
            val centerX = (100..(getScreenWidth() - 100)).random()
            Log.e(TAG, "onCreate: centerX = $centerX, px2dp = ${centerX.toFloat().px2dp}")
            val newPv = PointView(this).apply {
                id = it
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                confirmPosition(centerX.toFloat())
            }
            Log.e(TAG, "onCreate: newPv.TAG =  ${newPv.TAG}, newPv.id = ${newPv.id}", )
            pointViewMap[it] = newPv
            binding.root.addView(newPv)
            startMoving(newPv)
        })

        viewModel.generatePointViewOnTime()



//        startMoving(binding.pointView2)

        Log.e(TAG, "onCreate: getScreenWidth() = " + getScreenWidth())








        //todo startMoving

//        binding.pointView.apply {
//            animate().apply {
//                interpolator = AccelerateInterpolator()
//                duration = 10000
//                translationY(10000f)
//                start()
//            }
//
//            setOnClickListener {
//                clearAnimation()
//            }
//        }

    }

    private fun startMoving(view: View) {
        view.apply {
            animate().apply {
                interpolator = AccelerateInterpolator()
                duration = 10000
                translationY(10000f)
                start()
            }

//            animation?.setAnimationListener(object: Animation.AnimationListener {
//                override fun onAnimationStart(p0: Animation?) {
//                }
//
//                override fun onAnimationEnd(p0: Animation?) {
//
//                }
//
//                override fun onAnimationRepeat(p0: Animation?) {
//                }
//
//            })

//            setOnClickListener {
//                Log.e(TAG, "startMoving: click ${view.id}")
//                pointViewMap[it.id]?.apply {
//                    visibility = View.GONE
//                    animation?.cancel()
//                    clearAnimation()
//                }

//                it.clearAnimation ()
//            }

        }
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}