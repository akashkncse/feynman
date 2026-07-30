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
    private val padding=40f

    fun saveSlides(slides:List<Slide>,slidesDir:File,screenWidth:Int,screenHeight:Int){
        slides.forEachIndexed { index, slide ->
            val bitmap=Bitmap.createBitmap(slideWidth,slideHeight,Bitmap.Config.ARGB_8888)
            val canvas=Canvas(bitmap)
            canvas.drawColor(Color.BLACK)
            val bounds=renderer.computeBounds(slide)
            if(bounds!=null){
                val left=(bounds.left-padding).coerceAtLeast(0f)
                val top=(bounds.top-padding).coerceAtLeast(0f)
                val right=(bounds.right+padding).coerceAtMost(screenWidth.toFloat())
                val bottom=(bounds.bottom+padding).coerceAtMost(screenHeight.toFloat())

                val usedWidth=(right-left).coerceAtLeast(1f)
                val usedHeight=(bottom-top).coerceAtLeast(1f)

                val scale=minOf(slideWidth/usedWidth,slideHeight/usedHeight)

                val scaledContentWidth=usedWidth*scale
                val scaledContentHeight=usedHeight*scale
                val offsetX=(slideWidth-scaledContentWidth)/2f
                val offsetY=(slideHeight-scaledContentHeight)/2f

                canvas.save()
                canvas.translate(offsetX,offsetY)
                canvas.scale(scale,scale)
                canvas.translate(-left,-top)
                renderer.render(canvas,slide)
                canvas.restore()
            }

            val file=File(slidesDir,"slide_$index.png")
            FileOutputStream(file).use{
                out->
                bitmap.compress(Bitmap.CompressFormat.PNG,100,out)
            }
            bitmap.recycle()
        }
    }
}