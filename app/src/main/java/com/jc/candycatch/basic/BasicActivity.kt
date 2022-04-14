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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBasicBinding.inflate(layoutInflater)
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
                    if (viewMap.isEmpty()) {
                        Log.e(TAG, "onAnimationEnd: viewMap.isEmpty", )
                        ResultDialog.newInstance(this@BasicActivity, catchNumber++)
                            .show(supportFragmentManager.beginTransaction(), "ResultDialog")
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

//    private fun doVibratorEffect() {
//        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val vibratorManager =
//                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
//            vibratorManager.defaultVibrator
//        } else {
//            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
//        } else {
//            vibrator.vibrate(50)
//        }
//    }

    override fun onDismiss() {

        viewModel.generatePointViewOnTime()
    }

    override fun playAgain() {
        viewMap.clear()
        catchNumber = 0
        binding.apply {
//            leftTimeTv.text = getString(R.string.left_time_double, 15)
            binding.catchNumberTv.text = getString(R.string.catch_number, catchNumber)
        }
        showCountDownDialog()
    }

    private fun showCountDownDialog() {
        CountDownDialog.newInstance(this)
            .show(supportFragmentManager.beginTransaction(), "CountDownDialog")
    }
}