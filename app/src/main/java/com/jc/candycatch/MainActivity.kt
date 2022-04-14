package com.jc.candycatch

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import com.jc.candycatch.advanced.AdvancedActivity
import com.jc.candycatch.basic.BasicActivity
import com.jc.candycatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
//    , CountDownDialog.DialogDismissListener,
//    ResultDialog.PlayAgainListener {

//    private val TAG = "MainActivity"
//    private var catchNumber = 0
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var viewModel: MainViewModel
//    private val movingDuration = 3000L
//    private val viewMap: MutableMap<Int, TextView> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            basicBtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, BasicActivity::class.java))
            }
            advancedBtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, AdvancedActivity::class.java))
            }
        }

//        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
//        viewModel.pointViewLd.observe(this, {
//            val leftMargin = (0..(getScreenWidth() - 140)).random()
//            TextView(this).apply {
//                Log.e(TAG, "onCreate: new TextView, set id  = $it")
//                id = it
//                layoutParams = FrameLayout.LayoutParams(140, 140).apply {
//                    setMargins(leftMargin, -140, 0, 0)
//                }
//                background = ContextCompat.getDrawable(this@MainActivity, generateRandomCandy())
//                binding.root.addView(this)
//                startMoving(this)
//
//                setOnTouchListener(object : View.OnTouchListener {
//                    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
//                        Log.e(TAG, "onTouch: view.id = ${view?.id}", )
//
//                        if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
//                            view?.visibility = View.GONE
//                            catchNumber++
//                            binding.catchNumberTv.text =
//                                getString(R.string.catch_number, catchNumber)
//                            doVibratorEffect()
//
//                            view?.id?.let { idKey ->
//                                viewMap.remove(idKey)
//                            }
//
//                            return true
//                        }
//
//                        return false
//                    }
//                })
//
//                viewMap[id] = this
//            }
//        })


//        viewModel.countDownTimeLiveData.observe(this, {
//            val leftTimeCount = 15 - it
//            if (it >= 6) {
//                binding.leftTimeTv.text = getString(R.string.left_time_single, leftTimeCount)
//            } else {
//                binding.leftTimeTv.text = getString(R.string.left_time_double, leftTimeCount)
//            }
//            if (leftTimeCount == 0) {
//                Handler(Looper.getMainLooper()).postDelayed({
//                    ResultDialog.newInstance(this, catchNumber++)
//                        .show(supportFragmentManager.beginTransaction(), "ResultDialog")
//                }, 3000)
//            }
//        })

//        showCountDownDialog()
    }

//    private fun startMoving(tv: TextView) {
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
//
//    }

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

//    override fun onDismiss() {

//        viewModel.generatePointViewOnTime()
//    }

//    override fun playAgain() {
//        catchNumber = 0
//        binding.apply {
////            leftTimeTv.text = getString(R.string.left_time_double, 15)
//            binding.catchNumberTv.text = getString(R.string.catch_number, catchNumber)
//        }
//        showCountDownDialog()
//    }

//    private fun showCountDownDialog() {
//        CountDownDialog.newInstance(this)
//            .show(supportFragmentManager.beginTransaction(), "CountDownDialog")
//    }

}