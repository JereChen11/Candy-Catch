package com.jc.candycatch.advanced

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.jc.candycatch.BaseActivity
import com.jc.candycatch.R
import com.jc.candycatch.databinding.ActivityAdvancedBinding
import com.jc.candycatch.utils.*
import kotlin.math.absoluteValue

class AdvancedActivity : BaseActivity<ActivityAdvancedBinding>() {

    override fun initView() {
        showCountDownDialog(R.string.level_advanced)

        startRotateIssueTv()
    }

    override fun initObserve() {
        super.initObserve()

        viewModel.pointViewLd.observe(this, {
            TextView(this).apply {
                id = it
                background =
                    ContextCompat.getDrawable(this@AdvancedActivity, generateRandomCandy())
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

    private fun startRotateIssueTv() {
        ObjectAnimator.ofFloat(viewBinding.issueIv, "rotation", 0f, 360f).apply {
            duration = 2000
            repeatMode = ObjectAnimator.RESTART
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
    }

    private fun startMoving(tv: TextView) {
        val transitionPair = calculatePosition(generateRandomX(), generateRandomY())
        tv.layoutParams = ConstraintLayout.LayoutParams(tvWidth, tvHeight).apply {
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

    private fun calculatePosition(x: Int, y: Int): Pair<Int, Int> {
        return checkTransitionXy(Pair(x - halfScreenWidth, y - halfScreenHeight))
    }

    /**
     * 取一个相同大小的三角形的话，还是有可能平移不出屏幕，应该避免这个情况
     */
    private fun checkTransitionXy(transitionXyPair: Pair<Int, Int>): Pair<Int, Int> {
        //递归的终止条件
        if (transitionXyPair.first.absoluteValue > (halfScreenWidth + tvWidth)
            || transitionXyPair.second.absoluteValue > (halfScreenHeight + tvHeight)
        ) {
            return transitionXyPair
        }

        return checkTransitionXy(
            Pair(
                (transitionXyPair.first * 1.1).toInt(),
                (transitionXyPair.second * 1.1).toInt()
            )
        )
    }

}