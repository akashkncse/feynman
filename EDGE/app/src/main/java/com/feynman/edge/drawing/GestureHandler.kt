package com.feynman.edge.drawing

import android.view.MotionEvent

class GestureHandler (
    private val engine: WhiteBoardEngine,
    private val OnStorkeUpdated:()-> Unit
){
    fun handleTouch(event: MotionEvent,currTool: Tool,penColor:Int): Boolean{
        val x=event.x
        val y=event.y
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                engine.startStroke(x,y,currTool,penColor)
            }

            MotionEvent.ACTION_MOVE->{
                engine.addPoint(x,y)
            }

            MotionEvent.ACTION_UP->{
                engine.finishStroke()
            }
            else->return false
        }
        OnStorkeUpdated()
        return true
    }
}