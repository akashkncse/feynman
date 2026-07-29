package com.feynman.edge.storage

import android.content.Context
import android.media.metrics.LogSessionId
import java.io.File

class FileManager(private val context: Context){
    fun getSessionDir(sessionId: String):File{
        val baseDir=File(context.filesDir,"sessions")
        val sessionDir=File(baseDir,sessionId)
        if(!sessionDir.exists()) sessionDir.mkdirs()
        return sessionDir
    }
    fun getSlidesDir(sessionId: String):File{
        val dir=File(getSessionDir(sessionId),"slides")
        if(!dir.exists()) dir.mkdirs()
        return dir
    }
}