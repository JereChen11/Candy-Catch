package com.jc.candycatch.difficult

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.jc.candycatch.R
import com.jc.candycatch.databinding.ActivityDifficultBinding
import com.jc.candycatch.utils.doVibratorEffect
import com.jc.candycatch.utils.generateRandomCandy
import com.jc.candycatch.utils.getScreenHeight
import com.jc.candycatch.utils.getScreenWidth
import com.jc.candycatch.view.CountDownDialog
import com.jc.candycatch.view.ResultDialog

class DifficultActivity : AppCompatActivity(), CountDownDialog.DialogDismissListener,
    ResultDialog.PlayAgainListener {

    private lateinit var binding: ActivityDifficultBinding
    private lateinit var viewModel: DifficultViewModel

    private val viewMap: MutableMap<Int, TextView> = HashMap()
    private var catchNumber = 0
    private var isShowResultDialog = false

    private val TAG = "DifficultActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDifficultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPitchingIv()

        viewModel = ViewModelProvider(this)[DifficultViewModel::class.java]


        Log.e(TAG, "onCreate: Direction.values().random() = ${Direction.values().random()}")

        viewModel.pointViewLd.observe(this, {
            TextView(this).apply {
                id = it
                background =
                    ContextCompat.getDrawable(this@DifficultActivity, generateRandomCandy())
                binding.root.addView(this)

                viewMap[id] = this

                startMoving(this)

                setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {

                        if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
                            doVibratorEffect()
                            view?.visibility = View.GONE

                            catchNumber++
                            binding.catchNumberTv.text =
                                getString(R.string.catch_number, catchNumber)

                            view?.id?.let { idKey ->
                                viewMap.remove(idKey)
                            }

                            return true
                        }

                        return false
                    }
                })
            }
        })

        showCountDownDialog()

    }

    private val tvWidth = 140
    private val tvHeight = 140
    private val screenWidth = getScreenWidth()
    private val screenHeight = getScreenHeight()
    private val halfScreenWidth = screenWidth / 2
    private val halfScreenHeight = screenHeight / 2

    private fun startMoving(tv: TextView) {
        //step 1：确定Tv的初始位置
        var transitionPair: Pair<Int, Int> = Pair(0, 0)
        tv.layoutParams = when (Direction.values().random()) {
            Direction.LEFT_TOP -> {
                transitionPair = calculatePosition(
                    generateRandomX(screenWidth / 2),
                    generateRandomY(screenHeight / 2)
                )
                ConstraintLayout.LayoutParams(tvWidth, tvHeight).apply {
                    startToStart = ConstraintSet.PARENT_ID
                    topToTop = ConstraintSet.PARENT_ID
                }
            }
            Direction.LEFT_BOTTOM -> {
                transitionPair = calculatePosition(
                    generateRandomX(screenWidth / 2),
                    generateRandomY(0, screenHeight / 2)
                )
                ConstraintLayout.LayoutParams(tvWidth, tvHeight).apply {
                    startToStart = ConstraintSet.PARENT_ID
                    bottomToBottom = ConstraintSet.PARENT_ID
                }
            }
            Direction.RIGHT_TOP -> {
                transitionPair = calculatePosition(
                    generateRandomX(0, screenWidth / 2),
                    generateRandomY(screenHeight / 2)
                )
                ConstraintLayout.LayoutParams(tvWidth, tvHeight).apply {
                    endToEnd = ConstraintSet.PARENT_ID
                    topToTop = ConstraintSet.PARENT_ID
                }
            }
            Direction.RIGHT_BOTTOM -> {
                transitionPair = calculatePosition(
                    generateRandomX(0, screenWidth / 2),
                    generateRandomY(0, screenHeight / 2)
                )
                ConstraintLayout.LayoutParams(tvWidth, tvHeight).apply {
                    endToEnd = ConstraintSet.PARENT_ID
                    bottomToBottom = ConstraintSet.PARENT_ID
                }
            }
        }

        //step 2：确定移动的最终点
//        val transitionPair = calculatePosition(generateRandomX(), generateRandomY())
        val animX = ObjectAnimator.ofFloat(tv, "translationX", transitionPair.first.toFloat())
        val animY = ObjectAnimator.ofFloat(tv, "translationY", transitionPair.second.toFloat())
        AnimatorSet().apply {
            playTogether(animX, animY)
            duration = 4000
            interpolator = AccelerateInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    viewMap.remove(tv.id)
                    if (viewMap.isEmpty() && !isShowResultDialog
                        && !supportFragmentManager.isDestroyed
                    ) {
                        ResultDialog.newInstance(this@DifficultActivity, catchNumber++)
                            .show(supportFragmentManager.beginTransaction(), "ResultDialog")
                        isShowResultDialog = true
                    }
                }

                override fun onAnimationCancel(p0: Animator?) {

                }

                override fun onAnimationRepeat(p0: Animator?) {

                }

            })
            start()
        }

    }

    private fun generateRandomX(leftRange: Int = 0, rightRange: Int = getScreenWidth()): Int {
        return (leftRange..rightRange).random()
    }

    private fun generateRandomY(leftRange: Int = 0, rightRange: Int = getScreenHeight()): Int {
        return (leftRange..rightRange).random()
    }

    private fun calculatePosition(x: Int, y: Int): Pair<Int, Int> {
        //移动的距离是两个相似三角形相加
        return if (x > halfScreenWidth) {
            if (y > halfScreenHeight) {
                //right_bottom
                Pair(x * 2, y * 2)
            } else {
                //right_top
                Pair(x * 2, -2 * (screenHeight - y))
            }
        } else {
            if (y > halfScreenHeight) {
                //left_bottom
                Pair(-2 * (screenWidth - x), 2 * y)
            } else {
                //left_top
                Pair(-2 * (screenWidth - x), -2 * (screenHeight - y))
            }
        }
    }

    private fun initPitchingIv() {
        binding.apply {
            startRotateIssueTv(leftTopIv)
            startRotateIssueTv(rightTopIv)
            startRotateIssueTv(leftBottomIv)
            startRotateIssueTv(rightBottomIv)
        }

    }

    private fun startRotateIssueTv(iv: ImageView) {
        ObjectAnimator.ofFloat(iv, "rotation", 0f, 360f).apply {
            duration = 3000
            repeatMode = ObjectAnimator.RESTART
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
    }

    enum class Direction {
        LEFT_TOP,
        RIGHT_TOP,
        LEFT_BOTTOM,
        RIGHT_BOTTOM
    }

    override fun onDismiss() {
        viewModel.generatePointViewOnTime()
    }

    override fun playAgain() {
        viewMap.clear()
        catchNumber = 0
        isShowResultDialog = false

        viewModel.generatePointViewOnTime()
    }

    override fun backHomePage() {
        finish()
    }

    private fun showCountDownDialog() {
        CountDownDialog.newInstance(this)
            .show(supportFragmentManager.beginTransaction(), "CountDownDialog")
    }
}