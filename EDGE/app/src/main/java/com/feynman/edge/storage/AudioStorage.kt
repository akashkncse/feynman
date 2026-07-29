package com.feynman.edge.storage

import java.io.File
class AudioStorage {
    fun saveAudio(sourceFile: File?,sessionDir:File):File?{
        if(sourceFile==null||!sourceFile.exists()) return null
        val destFile=File(sessionDir,"audio.m4a")
        sourceFile.copyTo(destFile, overwrite = true)
        return destFile
    }
}