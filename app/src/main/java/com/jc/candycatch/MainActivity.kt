package com.jc.candycatch

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.jc.candycatch.databinding.ActivityMainBinding
import com.jc.candycatch.utils.getScreenHeight
import com.jc.candycatch.utils.getScreenWidth

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var catchNumber = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.pointViewLiveData.observe(this, {
            val centerX = (0..(getScreenWidth() - 120)).random()
            TextView(this).apply {
                layoutParams = FrameLayout.LayoutParams(120, 120).apply {
                    setMargins(centerX, -120, 0, 0)
                }
                background = ContextCompat.getDrawable(this@MainActivity, generateRandomCandy())
                binding.root.addView(this)
                startMoving(this)

                setOnClickListener {
                    this.visibility = View.GONE
                    Log.e(TAG, "onCreate: tag = ${it.tag}, id = ${it.id}")
                    catchNumber++
                    binding.catchNumberTv.text = catchNumber.toString()
                }
            }
        })

        viewModel.generatePointViewOnTime()
    }

    private fun generateRandomCandy(): Int {
        return when((0..9).random()) {
            0 -> R.drawable.svg_candy1
            1 -> R.drawable.svg_candy2
            2 -> R.drawable.svg_candy3
            3 -> R.drawable.svg_candy4
            4 -> R.drawable.svg_candy5
            5 -> R.drawable.svg_candy6
            6 -> R.drawable.svg_candy7
            7 -> R.drawable.svg_candy8
            8 -> R.drawable.svg_candy9
            else -> R.drawable.svg_candy10
        }
    }

    private fun startMoving(view: View) {
        view.apply {
            animate().apply {
                interpolator = AccelerateInterpolator()
                duration = 3000
                translationY(getScreenHeight().toFloat() + 200)
                start()
            }
        }
    }


}