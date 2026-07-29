package com.feynman.edge.drawing

data class Stroke(
    val points: MutableList<Point> =mutableListOf(),
    val tool:Tool=Tool.PEN,
    val color:Int=0xFF000000.toInt(),
    val strokeWidth:Float=8f

)