package com.jc.candycatch.view

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import com.jc.candycatch.R
import com.jc.candycatch.databinding.FragmentDialogCountDownBinding
import java.util.*
import kotlin.concurrent.timerTask

class CountDownDialog : DialogFragment() {
    private var binding: FragmentDialogCountDownBinding? = null
    private var countDownNumber = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.dialog_style)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogCountDownBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            Timer().apply {
                scheduleAtFixedRate(timerTask {
                    if (countDownNumber <= 1) {
                        cancel()
                    }
                    Log.e("jctest", "onViewCreated: countDownNumber = $countDownNumber")

                    countDownTv.post {
                        countDownTv.text = countDownNumber.toString()
                    }
                    countDownNumber--
                }, 1000, 1000)
            }
        }
        handClickAnimation()
    }


    private fun handClickAnimation() {
        binding?.apply {
            handClickTv.apply {
                val animation = AnimationUtils.loadAnimation(context, R.anim.hand_click_anim)
                startAnimation(animation)
            }
            ringView.apply {
                val animation = AnimationUtils.loadAnimation(context, R.anim.ring_anim)
                startAnimation(animation)
            }
            candyAddOneTv.apply {
                val animation = AnimationUtils.loadAnimation(context, R.anim.candy_add_anim)
                startAnimation(animation)
            }
        }




        val oa = ObjectAnimator.ofFloat(binding?.handClickTv, "translationX", -100f).apply {
            duration = 1000
            repeatCount = Animation.INFINITE
//            repeatMode = Animation.RESTART
//            repeatMode = 10
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {

        fun newInstance(): CountDownDialog {
            return CountDownDialog().apply {

            }
        }
    }

}