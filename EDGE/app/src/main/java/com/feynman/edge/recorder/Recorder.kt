package com.feynman.edge.recorder

import android.content.Context
import android.media.MediaRecorder
import java.io.File
import java.io.IOException

class Recorder(private val context:Context){
    private var mediaRecorder: MediaRecorder?=null
    private var outputFile:File?=null

    fun start():File?{
        val file=File(context.cacheDir,"temp_audio_${System.currentTimeMillis()}.m4a")
        outputFile=file

        var recorder=if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.S){
            MediaRecorder(context)
        }
        else{
            @Suppress("Deprecation")
            MediaRecorder()
        }
        try{
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            recorder.setOutputFile(file.absolutePath)
            recorder.prepare()
            recorder.start()
            mediaRecorder=recorder
            return file
        }
        catch (e:IOException){
            e.printStackTrace()
            return null
        }
    }

    fun stop():File?{
        try{
            mediaRecorder?.apply {
                stop()
                release()
            }
        }
        catch (e: RuntimeException){
            e.printStackTrace()
        }
        mediaRecorder=null
        return outputFile
    }
}