package com.feynman.edge.network

import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.File

data class UploadMetaData(
    val sessionId:String,
    val dept:String,
    val year:String,
    val section:String,
    val date:String,
    val startTime:Long,
    val endTime:Long?
)
class UploadManager {
    private val gson = GsonBuilder().create()
    fun uploadSession(
        sessionDir: File,
        metadata: UploadMetaData,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val metadataJson = gson.toJson(metadata)
        val metadataBody = metadataJson.toRequestBody("application/json".toMediaTypeOrNull())
        val audioFile = File(sessionDir, "audio.m4a")
        if (!audioFile.exists()) {
            onFailure("AudioFile Missing, Cannot upload")
            return
        }
        val audioRequestBody = audioFile.asRequestBody("audio/mp4".toMediaTypeOrNull())
        val audioPart = MultipartBody.Part.createFormData("audio", audioFile.name, audioRequestBody)

        val slidesDir = File(sessionDir, "slides")
        val imageParts = mutableListOf<MultipartBody.Part>()

        slidesDir.listFiles()?.sortedBy{it.name}?.forEach { imageFile->
            val imageRequestBody=imageFile.asRequestBody("image/png".toMediaTypeOrNull())
            imageParts.add(
                MultipartBody.Part.createFormData("images",imageFile.name,imageRequestBody)
            )
        }

        val call= ApiClient.apiService.uploadLecture(metadataBody,audioPart,imageParts)

        call.enqueue(object : Callback<okhttp3.ResponseBody> {
            override fun onResponse(call: Call<okhttp3.ResponseBody?>, response: Response<okhttp3.ResponseBody?>) {
                if(response.isSuccessful){
                    onSuccess()
                }
                else{
                    onFailure("Server responded with ${response.code()}")
                }
            }

            override fun onFailure(call: Call<okhttp3.ResponseBody?>, t: Throwable) {
                onFailure("Upload Failed: ${t.message}")
            }
        })

    }
}