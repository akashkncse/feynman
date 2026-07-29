package com.feynman.edge.ui

import android.content.Context
import android.widget.Button
import android.widget.LinearLayout
import com.feynman.edge.drawing.Tool

class Toolbar (context: Context,private val canvasView: CanvasView): LinearLayout(context){
    init{
        orientation=HORIZONTAL
        addButton("Eraser"){
            canvasView.currTool= Tool.ERASER
        }
        addButton("Pen"){
            canvasView.currTool=Tool.PEN
        }
        addButton("Undo"){
            canvasView.engine.doundo()
            canvasView.refresh()
        }
        addButton("Redo"){
            canvasView.engine.doredo()
            canvasView.refresh()
        }
        addButton("Clear"){
            canvasView.engine.clear()
            canvasView.refresh()
        }
        addButton("Prev"){
            canvasView.engine.prevSlide()
            canvasView.refresh()
        }
        addButton("Next"){
            canvasView.engine.nextSlide()
            canvasView.refresh()
        }
    }
    private fun addButton(label: String,onClick:()->Unit){
        val button=Button(context).apply {
            text=label
            setOnClickListener { onClick() }
        }
        addView(button)
    }
}