package com.jc.candycatch.difficult

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.jc.candycatch.BaseActivity
import com.jc.candycatch.R
import com.jc.candycatch.databinding.ActivityDifficultBinding
import com.jc.candycatch.utils.*

class DifficultActivity : BaseActivity<ActivityDifficultBinding>() {

    override fun initView() {
        showCountDownDialog(R.string.level_difficult)

        initPitchingIv()
        viewModel.delayTime = 25000L
    }

    override fun initObserve() {
        super.initObserve()

        viewModel.pointViewLd.observe(this, {
            TextView(this).apply {
                id = it
                background =
                    ContextCompat.getDrawable(this@DifficultActivity, generateRandomCandy())
                viewBinding.root.addView(this)

                viewMap[id] = this

                startMoving(this)

                setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {

                        if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
                            doVibratorEffect()
                            view?.visibility = View.GONE

                            catchNumber++
                            viewBinding.catchNumberTv.text =
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
    }

    override fun playAgain() {
        super.playAgain()
        viewBinding.catchNumberTv.text = getString(R.string.catch_number, catchNumber)
    }

    private fun startMoving(tv: TextView) {
        //step 1：确定Tv的初始位置
        var transitionPair: Pair<Int, Int> = Pair(0, 0)
        tv.layoutParams = when (Direction.values().random()) {
            Direction.LEFT_TOP -> {
                transitionPair = calculatePosition(
                    generateRandomX(halfScreenWidth),
                    generateRandomY(halfScreenHeight)
                )
                ConstraintLayout.LayoutParams(tvWidth, tvHeight).apply {
                    startToStart = ConstraintSet.PARENT_ID
                    topToTop = ConstraintSet.PARENT_ID
                }
            }
            Direction.LEFT_BOTTOM -> {
                transitionPair = calculatePosition(
                    generateRandomX(halfScreenWidth),
                    generateRandomY(0, halfScreenHeight)
                )
                ConstraintLayout.LayoutParams(tvWidth, tvHeight).apply {
                    startToStart = ConstraintSet.PARENT_ID
                    bottomToBottom = ConstraintSet.PARENT_ID
                }
            }
            Direction.RIGHT_TOP -> {
                transitionPair = calculatePosition(
                    generateRandomX(0, halfScreenWidth),
                    generateRandomY(halfScreenHeight)
                )
                ConstraintLayout.LayoutParams(tvWidth, tvHeight).apply {
                    endToEnd = ConstraintSet.PARENT_ID
                    topToTop = ConstraintSet.PARENT_ID
                }
            }
            Direction.RIGHT_BOTTOM -> {
                transitionPair = calculatePosition(
                    generateRandomX(0, halfScreenWidth),
                    generateRandomY(0, halfScreenHeight)
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
                    showResultDialog()
                }

                override fun onAnimationCancel(p0: Animator?) {

                }

                override fun onAnimationRepeat(p0: Animator?) {

                }

            })
            start()
        }

    }

    /**
     * x, y 是指生成的随机坐标点的坐标
     *
     * 移动的距离是两个相似三角形相加
     */
    private fun calculatePosition(x: Int, y: Int): Pair<Int, Int> {
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
        viewBinding.apply {
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

}