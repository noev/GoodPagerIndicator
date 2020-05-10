package com.goodrequest.base

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

abstract class SingleChildPagerIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseGoodPagerIndicator(context, attrs, defStyleAttr) {

    abstract fun onMeasureDot(widthMeasureSpec: Int, heightMeasureSpec: Int): Pair<Int, Int>

    abstract fun onDrawDot(canvas: Canvas, position: Int)

    override fun onScroll(itemCount: Int, position: Int, positionOffset: Float) {
        if (childCount != 1) {
            addView(Dot(context).apply {
                drawing = { canvas, position -> onDrawDot(canvas, position) }
                measuring = { w, h -> onMeasureDot(w, h) }
            })
        }

        (getChildAt(0) as Dot).invalidate()
    }

    private inner class Dot @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : View(context, attrs, defStyleAttr) {

        var drawing: (Canvas, Int) -> Unit = { _, _ -> }
        var measuring: (widthMeasureSpec: Int, heightMeasureSpec: Int) -> Pair<Int, Int> = { w, h -> w to h }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            val (widthSpec, heightSpec) = measuring(widthMeasureSpec, heightMeasureSpec)
            setMeasuredDimension(widthSpec, heightSpec)
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            drawing(canvas, position)
        }
    }
}