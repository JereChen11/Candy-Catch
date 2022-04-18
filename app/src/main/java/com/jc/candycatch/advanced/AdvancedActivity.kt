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

class AdvancedActivity : BaseActivity<ActivityAdvancedBinding>() {

    override fun initView() {
        super.initView()

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
        val halfScreenWidth = getScreenWidth() / 2
        val halfScreenHeight = getScreenHeight() / 2
        //todo 相似三角形
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

}