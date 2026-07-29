package com.feynman.edge.session

import com.feynman.edge.drawing.Slide
import java.io.File

data class LectureSession(
    val sessionId:String,
    val dept:String,
    val year:String,
    val section:String,
    val startTime:Long,
    var endTime:Long?=null,
    val slides: MutableList<Slide> =mutableListOf(),
    var audio:File?=null,
    val date:String
)