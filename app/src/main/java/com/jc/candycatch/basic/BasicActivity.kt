package com.jc.candycatch.basic

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.*
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.jc.candycatch.BaseActivity
import com.jc.candycatch.R
import com.jc.candycatch.databinding.ActivityBasicBinding
import com.jc.candycatch.utils.doVibratorEffect
import com.jc.candycatch.utils.generateRandomCandy
import com.jc.candycatch.utils.getScreenHeight
import com.jc.candycatch.utils.getScreenWidth

class BasicActivity : BaseActivity<ActivityBasicBinding>() {

    private val movingDuration = 3000L

    override fun initView() {
        showCountDownDialog(R.string.level_basic)
    }

    override fun initObserve() {
        viewModel.pointViewLd.observe(this, {
            val leftMargin = (0..(getScreenWidth() - tvWidth)).random()
            TextView(this).apply {
                id = it
                layoutParams = FrameLayout.LayoutParams(tvWidth, tvHeight).apply {
                    setMargins(leftMargin, -tvWidth, 0, 0)
                }
                background = ContextCompat.getDrawable(this@BasicActivity, generateRandomCandy())
                viewBinding.root.addView(this)

                startMoving(this)

                setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
                        if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
                            view?.visibility = View.GONE
                            catchNumber++
                            viewBinding.catchNumberTv.text =
                                getString(R.string.catch_number, catchNumber)
                            doVibratorEffect()

                            view?.id?.let { idKey ->
                                viewMap.remove(idKey)
                            }

                            return true
                        }

                        return false
                    }
                })

                viewMap[id] = this
            }
        })
    }

    private fun startMoving(tv: TextView) {
        ObjectAnimator.ofFloat(tv, "y", getScreenHeight().toFloat() + 200).apply {
            interpolator = AccelerateInterpolator()
            duration = movingDuration
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

    override fun playAgain() {
        super.playAgain()
        viewBinding.catchNumberTv.text = getString(R.string.catch_number, catchNumber)
    }

}