package com.jc.candycatch.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import com.jc.candycatch.R
import com.jc.candycatch.databinding.FragmentDialogCountDownBinding
import java.util.*
import kotlin.concurrent.timerTask

class CountDownDialog : DialogFragment() {
    private var binding: FragmentDialogCountDownBinding? = null
    private var countDownNumber = 4
    private val timer by lazy { Timer() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.dialog_style)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
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

        handClickAnimation()
        binding?.apply {
            levelTv.text = arguments?.getString(LEVEL_KEY) ?: ""

            startTv.setOnClickListener {
                startContainerCl.visibility = View.GONE
                val countDownAnimation =
                    AnimationUtils.loadAnimation(context, R.anim.count_down_anim)
                timer.scheduleAtFixedRate(timerTask {
                    if (countDownNumber <= 1) {
                        cancel()
                        dialog?.dismiss()
                        dialogDismissListener?.onDismiss()
                    } else {
                        countDownTv.apply {
                            post {
                                visibility = View.VISIBLE
                                text = countDownNumber.toString()
                                startAnimation(countDownAnimation)
                            }
                        }
                        countDownNumber--
                    }
                }, 0, 1000)
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
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        timer.cancel()
        activity?.finish()
    }

    interface DialogDismissListener {
        fun onDismiss()
    }

    companion object {
        var dialogDismissListener: DialogDismissListener? = null
        const val LEVEL_KEY = "LEVEL_KEY"

        fun newInstance(level: String, listener: DialogDismissListener): CountDownDialog {

            this.dialogDismissListener = listener
            return CountDownDialog().apply {
                arguments = Bundle().apply {
                    putString(LEVEL_KEY, level)
                }
            }
        }
    }

}