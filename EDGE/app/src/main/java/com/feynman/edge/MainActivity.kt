package com.feynman.edge

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.feynman.edge.session.LectureSessionManager
import com.feynman.edge.session.SessionState
import com.feynman.edge.storage.SessionStorage
import com.feynman.edge.ui.CanvasView
import com.feynman.edge.ui.Toolbar
import com.feynman.edge.ui.StartEndBar
import com.feynman.edge.recorder.RecordingService
import com.feynman.edge.network.UploadManager
import com.feynman.edge.network.UploadMetaData
class MainActivity : AppCompatActivity() {

    private lateinit var canvasView: CanvasView
    private lateinit var sessionManager: LectureSessionManager
    private lateinit var startEndBar: StartEndBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = FrameLayout(this)

        canvasView = CanvasView(this)
        val darkMode = isDarkModeOn()
        canvasView.applyTheme(darkMode)
        root.setBackgroundColor(if (darkMode) android.graphics.Color.BLACK else android.graphics.Color.WHITE)

        val toolbar = Toolbar(this, canvasView)
        val sessionStorage = SessionStorage(this)
        val recordingService=RecordingService(this)
        sessionManager = LectureSessionManager(canvasView.engine, sessionStorage,recordingService)

        startEndBar = StartEndBar(
            context = this,
            onStartClicked = { showStartDialog() },
            onEndClicked = { handleEndRecording() }
        )



        canvasView.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        toolbar.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        )

        startEndBar.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.TOP or Gravity.CENTER_HORIZONTAL
        )

        root.addView(canvasView)
        root.addView(toolbar)
        root.addView(startEndBar)

        setContentView(root)
        if(androidx.core.content.ContextCompat.checkSelfPermission(this,android.Manifest.permission.RECORD_AUDIO)!=android.content.pm.PackageManager.PERMISSION_GRANTED){
            androidx.core.app.ActivityCompat.requestPermissions(
                this,arrayOf(android.Manifest.permission.RECORD_AUDIO),100
            )
        }
        updateUiForState()
    }

    private fun showStartDialog() {
        val deptInput = EditText(this).apply { hint = "Dept (e.g. CSE)" }
        val yearInput = EditText(this).apply { hint = "Year (e.g. 2)" }
        val sectionInput = EditText(this).apply { hint = "Section (e.g. A)" }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 20, 40, 20)
            addView(deptInput)
            addView(yearInput)
            addView(sectionInput)
        }

        AlertDialog.Builder(this)
            .setTitle("Start Lecture")
            .setView(layout)
            .setCancelable(false)
            .setPositiveButton("Start") { _, _ ->
                val dept = deptInput.text.toString().ifBlank { "N/A" }
                val year = yearInput.text.toString().ifBlank { "N/A" }
                val section = sectionInput.text.toString().ifBlank { "N/A" }

                sessionManager.startLecture(dept, year, section)
                canvasView.locked = false
                updateUiForState()
            }
            .show()
    }

    private fun handleEndRecording() {
        val session = sessionManager.endLecture()

        canvasView.engine.resetAllSlides()
        canvasView.locked = true
        canvasView.refresh()

        sessionManager.resetForNextLecture()
        updateUiForState()

        if (session != null) {
            val sessionDir = java.io.File(filesDir, "sessions/${session.sessionId}")
            val uploadMetadata = com.feynman.edge.network.UploadMetaData(
                sessionId = session.sessionId,
                dept = session.dept,
                year = session.year,
                section = session.section,
                date = session.date,
                startTime = session.startTime,
                endTime= session.endTime
            )

            com.feynman.edge.network.UploadManager().uploadSession(
                sessionDir = sessionDir,
                metadata = uploadMetadata,
                onSuccess = {
                    android.util.Log.d("FeynmanUpload", "Upload succeeded")
                },
                onFailure = { reason ->
                    android.util.Log.e("FeynmanUpload", "Upload failed: $reason")
                }
            )
        }
    }
    private fun isDarkModeOn(): Boolean {
        val uiMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        return uiMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
    }
    private fun updateUiForState() {
        val isRecording = sessionManager.getState() == SessionState.RECORDING
        startEndBar.showStartButton(!isRecording)
    }
}