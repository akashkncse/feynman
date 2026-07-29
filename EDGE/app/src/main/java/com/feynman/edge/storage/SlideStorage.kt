package com.feynman.edge.storage

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import com.feynman.edge.drawing.CanvasRenderer
import com.feynman.edge.drawing.Slide
import java.io.File
import java.io.FileOutputStream

class SlideStorage{
    private val renderer= CanvasRenderer()
    private val slideWidth=1280
    private val slideHeight=720

    fun saveSlides(slides:List<Slide>,slidesDir:File){
        slides.forEachIndexed { index, slide ->
            val bitmap=Bitmap.createBitmap(slideWidth,slideHeight,Bitmap.Config.ARGB_8888)
            val canvas=Canvas(bitmap)
            canvas.drawColor(Color.BLACK)
            renderer.render(canvas,slide)
            val file=File(slidesDir,"slide_$index.png")
            FileOutputStream(file).use{
                out->
                bitmap.compress(Bitmap.CompressFormat.PNG,100,out)
            }
            bitmap.recycle()
        }
    }
}