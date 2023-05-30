package com.example.prm1

import android.content.Context
import android.graphics.*
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.example.prm1.model.PathWithSettings
import com.example.prm1.model.SettingsNote

class PhotoView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var background: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }

    var note: SettingsNote = SettingsNote()
        set(value) {
            field = value
            invalidate()
        }

    private val defaultPaint = Paint()

    override fun onDraw(canvas: Canvas) { 
        drawBackground(canvas)
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas) {
        val textPaint = Paint().apply {
            color = note.color
            textSize = note.size
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(note.text, width / 2f, height / 2f, textPaint)
    }

    private fun drawBackground(canvas: Canvas) {
        background?.let {
            val rect = Rect(0, 0, width, height )
            canvas.drawBitmap(it, null, rect, defaultPaint )
        }
    }

    fun generateBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawBackground(canvas)
        drawText(canvas)
        return bitmap
    }

}