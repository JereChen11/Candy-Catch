package com.jc.candycatch.advanced

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.jc.candycatch.databinding.ActivityTestBinding
import com.jc.candycatch.utils.doVibratorEffect
import com.jc.candycatch.utils.generateRandomCandy
import com.jc.candycatch.utils.getScreenHeight
import com.jc.candycatch.utils.getScreenWidth

class AdvancedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestBinding
    private lateinit var viewModel: AdvancedViewModel
    private val TAG = "AdvancedActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AdvancedViewModel::class.java]

        startRotateIssueTv()

        viewModel.pointViewLd.observe(this, {
            TextView(this).apply {
                layoutParams = ConstraintLayout.LayoutParams(140, 140).apply {
                    bottomToBottom = ConstraintSet.PARENT_ID
                    topToTop = ConstraintSet.PARENT_ID
                    startToStart = ConstraintSet.PARENT_ID
                    endToEnd = ConstraintSet.PARENT_ID
//                setMargins(getScreenWidth() / 2, getScreenHeight() / 2, 0, 0)
                }
                background =
                    ContextCompat.getDrawable(this@AdvancedActivity, generateRandomCandy())
                binding.root.addView(this)

                Log.e(TAG, "before startMoving: tv.x = ${x}, tv.y = ${y}")
                startMoving(this)

                setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {

                        if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
                            doVibratorEffect()
                            view?.visibility = View.GONE

                            return true
                        }

                        return false
                    }
                })


            }
        })

    }

    private fun startRotateIssueTv() {
        ObjectAnimator.ofFloat(binding.issueIv, "rotation", 0f, 360f).apply {
            duration = 2000
            repeatMode = ObjectAnimator.RESTART
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
    }


    private fun generateRandomX(): Int = (0..getScreenWidth()).random()

    private fun generateRandomY(): Int = (0..getScreenHeight()).random()

    private fun startMoving(tv: TextView) {

        val transitionPair = calculatePosition(generateRandomX(), generateRandomY())
        Log.e(
            TAG,
            "startMoving: transitionPair.first.toFloat() = ${transitionPair.first.toFloat()}," +
                    " transitionPair.second.toFloat() = ${transitionPair.second.toFloat()}",
        )
        tv.layoutParams = ConstraintLayout.LayoutParams(140, 140).apply {
            bottomToBottom = ConstraintSet.PARENT_ID
            topToTop = ConstraintSet.PARENT_ID
            startToStart = ConstraintSet.PARENT_ID
            endToEnd = ConstraintSet.PARENT_ID
        }
        val animX = ObjectAnimator.ofFloat(tv, "translationX", transitionPair.first.toFloat())
        val animY = ObjectAnimator.ofFloat(tv, "translationY", transitionPair.second.toFloat())
        AnimatorSet().apply {
            playTogether(animX, animY)
            duration = 3000
            interpolator = AccelerateInterpolator()
            start()
        }

    }

    private fun calculatePosition(x: Int, y: Int): Pair<Int, Int> {
        val halfScreenWidth = getScreenWidth() / 2
        val halfScreenHeight = getScreenHeight() / 2

        Log.e(
            "TestActivity",
            "calculatePosition: x = $x, y = $y, " +
                    "halfScreenWidth = $halfScreenWidth, halfScreenHeight = $halfScreenHeight",
        )
        return if (x <= halfScreenWidth) {
            //x left
            if (y <= halfScreenHeight) {
                Pair(x - halfScreenWidth, -halfScreenHeight - 140)
            } else {
                //y down
                Pair(x - halfScreenWidth, halfScreenHeight + 140)
            }
        } else {
            //x right
            if (y <= halfScreenHeight) {
                Pair(x - halfScreenWidth, -halfScreenHeight - 140)
            } else {
                Pair(x - halfScreenWidth, halfScreenHeight + 140)
            }
        }
    }

    //相似三角形

    override fun onResume() {
        super.onResume()
        viewModel.generatePointViewOnTime()
    }

}