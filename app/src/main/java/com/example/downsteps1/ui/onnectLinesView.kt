package com.example.downsteps1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ConnectLinesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val lines = mutableListOf<LineData>()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 8f
        strokeCap = Paint.Cap.ROUND
    }

    fun addLine(startX: Float, startY: Float, endX: Float, endY: Float, color: Int) {
        lines.add(LineData(startX, startY, endX, endY, color))
        invalidate()
    }

    fun clearLines() {
        lines.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (line in lines) {
            paint.color = line.color
            canvas.drawLine(line.startX, line.startY, line.endX, line.endY, paint)
        }
    }

    data class LineData(
        val startX: Float,
        val startY: Float,
        val endX: Float,
        val endY: Float,
        val color: Int
    )
}