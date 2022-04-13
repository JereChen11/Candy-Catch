package com.jc.candycatch

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.jc.candycatch.databinding.ActivityTestBinding
import com.jc.candycatch.utils.getScreenHeight
import com.jc.candycatch.utils.getScreenWidth
import com.jc.candycatch.view.ResultDialog

class TestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestBinding
    private val TAG = "TestActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        ObjectAnimator.ofFloat(binding.issueIv, "rotation", 0f, 360f).apply {
//            duration = 2000
//            repeatMode = ObjectAnimator.RESTART
//            repeatCount = ObjectAnimator.INFINITE
//            interpolator = LinearInterpolator()
//            start()
//        }

        Log.e("TestActivity", "onCreate: getScreenWidth = ${getScreenWidth()}, getScreenHeight() = ${getScreenHeight()}", )

        TextView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(140, 140).apply {
                bottomToBottom = ConstraintSet.PARENT_ID
                topToTop = ConstraintSet.PARENT_ID
                startToStart = ConstraintSet.PARENT_ID
                endToEnd = ConstraintSet.PARENT_ID
//                setMargins(getScreenWidth() / 2, getScreenHeight() / 2, 0, 0)
            }
            background = ContextCompat.getDrawable(this@TestActivity, R.drawable.svg_candy3)
            binding.root.addView(this)
            Log.e(TAG, "before startMoving: tv.x = ${x}, tv.y = ${y}", )
            startMoving(this)

            /*
            setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {

                    if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
                        view?.visibility = View.GONE
                        return true
                    }

                    return false
                }
            })
             */


        }


    }


    private fun generateRandomX(): Int = (0..getScreenWidth()).random()

    private fun generateRandomY(): Int = (0..getScreenHeight()).random()

    private fun startMoving(tv: TextView) {

        val transitionPair = calculatePosition(generateRandomX(), generateRandomY())
        Log.e(TAG, "startMoving: transitionPair.first.toFloat() = ${transitionPair.first.toFloat()}, transitionPair.second.toFloat() = ${transitionPair.second.toFloat()}", )
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
            duration = 1000
            interpolator = AccelerateInterpolator()
            start()
        }

//        ObjectAnimator.ofFloat(tv, "y", getScreenHeight().toFloat() + 200).apply {
//            interpolator = AccelerateInterpolator()
//            duration = movingDuration
//            addListener(object : Animator.AnimatorListener {
//                override fun onAnimationStart(p0: Animator?) {
//
//                }
//
//                override fun onAnimationEnd(p0: Animator?) {
//                    viewMap.remove(tv.id)
//                    if (viewMap.isEmpty()) {
//                        ResultDialog.newInstance(this@MainActivity, catchNumber++)
//                            .show(supportFragmentManager.beginTransaction(), "ResultDialog")
//                    }
//                }
//
//                override fun onAnimationCancel(p0: Animator?) {
//
//                }
//
//                override fun onAnimationRepeat(p0: Animator?) {
//
//                }
//
//            })
//            start()
//
//        }

    }

    private fun calculatePosition(x: Int, y: Int): Pair<Int, Int> {
        val halfScreenWidth = getScreenWidth() / 2
        val halfScreenHeight = getScreenHeight() / 2
        Log.e("TestActivity", "calculatePosition: x = $x, y = $y, halfScreenWidth = $halfScreenWidth, halfScreenHeight = $halfScreenHeight", )
        return if (x <= halfScreenWidth) {
            //x left
            if (y <= halfScreenHeight) {
                Pair(x - halfScreenWidth, -halfScreenHeight)
            } else {
                //y down
                Pair(x - halfScreenWidth, halfScreenHeight)
            }
        } else {
            //x right
            if (y <= halfScreenHeight) {
                Pair(x - halfScreenWidth, -halfScreenHeight)
            } else {
                Pair(x - halfScreenWidth, halfScreenHeight)
            }
        }
    }

    //相似三角形
}