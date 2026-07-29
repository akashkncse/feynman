package com.feynman.edge.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.feynman.edge.drawing.CanvasRenderer
import com.feynman.edge.drawing.GestureHandler
import com.feynman.edge.drawing.Tool
import com.feynman.edge.drawing.WhiteBoardEngine
import android.graphics.Bitmap
class CanvasView(context: Context,attrs: AttributeSet?=null): View(context,attrs){
    val engine= WhiteBoardEngine()
    private val renderer= CanvasRenderer()
    var locked: Boolean = true
    private val gestureHandler= GestureHandler(
        engine=engine,
        OnStorkeUpdated = {invalidate()}
    )

    var currTool:Tool=Tool.PEN
    private var bitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(w>0 && h>0){
            bitmap= Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888)
            bitmapCanvas= Canvas(bitmap!!)
        }
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bmp=bitmap?:return
        val bmpCanvas=bitmapCanvas?:return
        bmpCanvas.drawColor(0,android.graphics.PorterDuff.Mode.CLEAR)
        renderer.render(bmpCanvas, engine.getCurrentSlide())
        renderer.renderActiveStroke(bmpCanvas,engine.getActiveStrokeForRender())
        canvas.drawBitmap(bmp,0f,0f,null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (locked) return true
        val consumed = gestureHandler.handleTouch(event, currTool, currentPenColor)
        return consumed || super.onTouchEvent(event)
    }
    private var currentPenColor: Int = android.graphics.Color.BLACK

    fun applyTheme(isDark: Boolean) {
        currentPenColor = if (isDark) android.graphics.Color.WHITE else android.graphics.Color.BLACK
    }
    fun refresh(){
        invalidate()
    }
}