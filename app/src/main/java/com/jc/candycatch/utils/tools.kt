package com.jc.candycatch.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.jc.candycatch.R

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun generateRandomX(leftRange: Int = 0, rightRange: Int = getScreenWidth()): Int {
    return (leftRange..rightRange).random()
}

fun generateRandomY(leftRange: Int = 0, rightRange: Int = getScreenHeight()): Int {
    return (leftRange..rightRange).random()
}

fun generateRandomCandy(): Int {
    return when ((0..9).random()) {
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

fun Context.doVibratorEffect() {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(50)
    }

}