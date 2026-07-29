package com.feynman.edge.recorder

import android.content.Context
import java.io.File

class RecordingService (context: Context){
    private val recorder= Recorder(context)
    fun startRecording():File?{
        return recorder.start()
    }
    fun endRecording():File?{
        return recorder.stop()
    }
}