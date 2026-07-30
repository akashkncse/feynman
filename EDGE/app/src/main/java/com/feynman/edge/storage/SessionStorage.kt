package com.feynman.edge.storage

import android.content.Context
import com.feynman.edge.session.LectureSession
import com.google.gson.GsonBuilder
import java.io.File

data class SessionMetaData(
    val sessionId: String,
    val dept: String,
    val year: String,
    val section: String,
    val startTime: Long,
    val endTime: Long?,
    val slideCount: Int,
    val date:String
)
class SessionStorage(context: Context){
    private val FileManager=FileManager(context)
    private val slideStorage= SlideStorage()
    private val audioStorage= AudioStorage()
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun saveSession(session: LectureSession,width:Int,height:Int):File{
        val sessionDir=FileManager.getSessionDir(session.sessionId)
        val slidesDir= FileManager.getSlidesDir(session.sessionId)
        slideStorage.saveSlides(session.slides,slidesDir,width,height)
        audioStorage.saveAudio(session.audio,sessionDir)
        val metadata= SessionMetaData(
            sessionId = session.sessionId,
            dept=session.dept,
            year=session.year,
            section=session.section,
            startTime = session.startTime,
            endTime = session.endTime,
            slideCount = session.slides.size,
            date=session.date

        )
        val metadataFile=File(sessionDir,"metadata.json")
        metadataFile.writeText(gson.toJson(metadata))
        return sessionDir

    }

}