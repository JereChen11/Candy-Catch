package com.jc.candycatch.basic

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.jc.candycatch.R
import com.jc.candycatch.databinding.ActivityBasicBinding
import com.jc.candycatch.utils.doVibratorEffect
import com.jc.candycatch.utils.generateRandomCandy
import com.jc.candycatch.utils.getScreenHeight
import com.jc.candycatch.utils.getScreenWidth
import com.jc.candycatch.view.CountDownDialog
import com.jc.candycatch.view.ResultDialog

class BasicActivity : AppCompatActivity(), CountDownDialog.DialogDismissListener,
    ResultDialog.PlayAgainListener {

    private lateinit var binding: ActivityBasicBinding
    private lateinit var viewModel: BasicViewModel

    private val movingDuration = 3000L
    private val viewMap: MutableMap<Int, TextView> = HashMap()
    private var catchNumber = 0
    private val TAG = "BasicActivity"
    private var isShowResultDialog = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[BasicViewModel::class.java]
        viewModel.pointViewLd.observe(this, {
            val leftMargin = (0..(getScreenWidth() - 140)).random()
            TextView(this).apply {
                Log.e(TAG, "onCreate: new TextView, set id  = $it")
                id = it
                layoutParams = FrameLayout.LayoutParams(140, 140).apply {
                    setMargins(leftMargin, -140, 0, 0)
                }
                background = ContextCompat.getDrawable(this@BasicActivity, generateRandomCandy())
                binding.root.addView(this)
                startMoving(this)

                setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
                        Log.e(TAG, "onTouch: view.id = ${view?.id}")

                        if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
                            view?.visibility = View.GONE
                            catchNumber++
                            binding.catchNumberTv.text =
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

        showCountDownDialog()
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
                    Log.e(TAG, "onAnimationEnd: viewMap.size = ${viewMap.size}")
                    if (viewMap.isEmpty() && !isShowResultDialog
                        && !supportFragmentManager.isDestroyed
                    ) {
                        Log.e(TAG, "onAnimationEnd: viewMap.isEmpty")
                        ResultDialog.newInstance(this@BasicActivity, catchNumber++)
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

    override fun onDismiss() {
        viewModel.generatePointViewOnTime()
    }

    override fun playAgain() {
        viewMap.clear()
        catchNumber = 0
        isShowResultDialog = false
//        showCountDownDialog()
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