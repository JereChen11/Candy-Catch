package com.jc.candycatch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class PointView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    val TAG = "PointView"

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#384455")
    }
    private var centerX = 0f
    private var centerY = 0f
    private var isDraw = true

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e(TAG, "onTouchEvent: ", )
        if (event?.action == MotionEvent.ACTION_DOWN) {
            isDraw = false
            invalidate()
        }
        return super.onTouchEvent(event)
    }


//    override fun performClick(): Boolean {
//        return super.performClick()
//        Log.e(TAG, "performClick: ", )
//    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = resolveSize(suggestedMinimumHeight, heightMeasureSpec)
        Log.e(TAG, "onMeasure: widthMeasureSpec = $widthMeasureSpec")
        Log.e(TAG, "onMeasure: heightMeasureSpec = $heightMeasureSpec")
        Log.e(TAG, "onMeasure: width = $width")
        Log.e(TAG, "onMeasure: height = $height")
        //the purpose is force to set timeSharingLayer onSizeChanged
        onSizeChanged(width, height, width, height)
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.e(TAG, "onLayout: left = $left, top = $left, right = $right, bottom = $bottom")
    }

    fun confirmPosition(centerX: Float) {
        this.centerX = centerX
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.e(TAG, "onDraw: centerX = $centerX, centerY = $centerY")
        Log.e(TAG, "onDraw: width = $width, height = $height")
//        Log.e(TAG, "onDraw: 25f.px = ${25f.px}")
        if (isDraw) {
            canvas?.drawCircle(centerX, centerY + 60f, 60f, paint)
        }
    }
}