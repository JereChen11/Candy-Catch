package com.jc.candycatch.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.jc.candycatch.R
import com.jc.candycatch.databinding.FragmentDialogResultBinding

class ResultDialog : DialogFragment() {

    private var binding: FragmentDialogResultBinding? = null

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
        binding = FragmentDialogResultBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val catchNumber = arguments?.getInt(CATCH_NUMBER_KEY, 0)
        binding?.apply {
            numberTv.text = catchNumber.toString()
            playAgainTv.setOnClickListener {
                dialog?.dismiss()
                playAgainListener?.playAgain()
            }
        }

        showAnimation(view)

    }

    private fun showAnimation(view: View) {
        view.scaleX = 0F
        view.scaleY = 0F

        //zoom in 放大；zoom out 缩小；normal 恢复正常
        val zoomInHolderX = PropertyValuesHolder.ofFloat("scaleX", 1.05F)
        val zoomInHolderY = PropertyValuesHolder.ofFloat("scaleY", 1.05F)
        val zoomOutHolderX = PropertyValuesHolder.ofFloat("scaleX", 0.8F)
        val zoomOutHolderY = PropertyValuesHolder.ofFloat("scaleY", 0.8F)
        val normalHolderX = PropertyValuesHolder.ofFloat("scaleX", 1F)
        val normalHolderY = PropertyValuesHolder.ofFloat("scaleY", 1F)
        val zoomIn = ObjectAnimator.ofPropertyValuesHolder(
            view,
            zoomInHolderX,
            zoomInHolderY
        )

        val zoomOut = ObjectAnimator.ofPropertyValuesHolder(
            view,
            zoomOutHolderX,
            zoomOutHolderY
        )
        zoomOut.duration = 400

        val normal = ObjectAnimator.ofPropertyValuesHolder(
            view,
            normalHolderX,
            normalHolderY
        )
        normal.duration = 500

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(zoomIn, zoomOut, normal)
        animatorSet.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    interface PlayAgainListener {
        fun playAgain()
    }

    companion object {
        private const val CATCH_NUMBER_KEY = "CATCH_NUMBER"
        var playAgainListener: PlayAgainListener? = null

        fun newInstance(listener: PlayAgainListener, catchNumber: Int): ResultDialog {
            this.playAgainListener = listener
            return ResultDialog().apply {
                arguments = Bundle().apply {
                    putInt(CATCH_NUMBER_KEY, catchNumber)
                }
            }
        }
    }

}