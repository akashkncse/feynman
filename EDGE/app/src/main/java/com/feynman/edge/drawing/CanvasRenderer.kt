package com.feynman.edge.drawing

import android.graphics.Canvas
import android.graphics.Paint

class CanvasRenderer {
    private val paint= Paint().apply{
        isAntiAlias=true
        style=Paint.Style.STROKE
        strokeJoin=Paint.Join.ROUND
        strokeCap=Paint.Cap.ROUND
    }
    fun render(canvas: Canvas, slide: Slide){
        for (stroke in slide.strokes){
            drawStroke(canvas,stroke)
        }
    }

    fun renderActiveStroke(canvas: Canvas,stroke: Stroke?){
        stroke?.let { drawStroke(canvas,it) }
    }

    private fun drawStroke(canvas: Canvas,stroke: Stroke){
        if(stroke.points.size<2) return
        paint.color=stroke.color
        paint.strokeWidth=stroke.strokeWidth
        if(stroke.tool==Tool.ERASER){
            paint.color=0x00000000
            paint.xfermode=android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR)
        }
        else{
            paint.color=stroke.color
            paint.xfermode=null
        }
        for(i in 0 until stroke.points.size-1){
            val p1=stroke.points[i]
            val p2=stroke.points[i+1]
            canvas.drawLine(p1.x,p1.y,p2.x,p2.y,paint)
        }
    }
}