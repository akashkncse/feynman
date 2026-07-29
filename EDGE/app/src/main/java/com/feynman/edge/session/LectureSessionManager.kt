package com.feynman.edge.session

import java.util.UUID
import com.feynman.edge.drawing.WhiteBoardEngine
import com.feynman.edge.storage.SessionStorage
import com.feynman.edge.recorder.RecordingService

class LectureSessionManager (
    private val whiteBoardEngine: WhiteBoardEngine,
    private val sessionStorage: SessionStorage,
    private val recordingService: RecordingService
){
    private var state: SessionState = SessionState.IDLE
    private var currSession: LectureSession?=null

    fun getState(): SessionState=state
    fun startLecture(dept:String,year: String,section:String){
        if(state!= SessionState.IDLE) return
        val now=System.currentTimeMillis()
        currSession = LectureSession(
            sessionId = UUID.randomUUID().toString(),
            dept = dept,
            year = year,
            section = section,
            startTime = now,
            date=(now/1000).toString()

        )
        state = SessionState.RECORDING
        recordingService.startRecording()
    }
    fun endLecture(): LectureSession?{
        if(state!= SessionState.RECORDING) return null
        val session=currSession?:return null
        session.endTime=System.currentTimeMillis()
        session.slides.clear()
        for(i in 0 until whiteBoardEngine.getSlideCount()){
            session.slides.add(whiteBoardEngine.getSlideAt(i))
        }
        session.audio=recordingService.endRecording()
        sessionStorage.saveSession(session)
        state= SessionState.STOPPED
        return session
    }

    fun resetForNextLecture() {
        state = SessionState.IDLE
        currSession = null
    }
}