package com.chaos.down

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import com.chaos.loader.DownloadListener
import com.chaos.loader.DownloadManager
import com.chaos.loader.Priority

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url1 = "https://qiniup-v.cztv.com/cztv/vod/2023/04/17/fb16ccfb3d6143495738b3630d51199f/fb16ccfb3d6143495738b3630d51199f_h264_800k_mp4.mp4"
        val url2 = "https://qiniup-v.cztv.com/cztv/vod/2023/04/20/a8c5cc3cf6508f4d91f83ebf61465e38/a8c5cc3cf6508f4d91f83ebf61465e38_h264_800k_mp4.mp4"
        val url3 = "https://qiniup-v.cztv.com/cztv/vod/2023/04/19/d6c8db6cf4342f445cfacf1f114fd680/d6c8db6cf4342f445cfacf1f114fd680_h264_800k_mp4.mp4"
        val url4 = "https://qiniup-v.cztv.com/cztv/vod/2023/04/19/d6c8db6cf4342f445cfacf1f114fd680/d6c8db6cf4342f445cfacf1f114fd680_h264_800k_mp4.mp4"

        val destination1 = getExternalFilesDir(null)?.absolutePath + "/file1.mp4"
        val destination2 = getExternalFilesDir(null)?.absolutePath + "/file2.mp4"
        val destination3 = getExternalFilesDir(null)?.absolutePath + "/file3.mp4"
        val destination4 = getExternalFilesDir(null)?.absolutePath + "/file4.mp4"

        val downloadButton = findViewById<Button>(R.id.downloadButton)
        val pauseButton = findViewById<Button>(R.id.pauseButton)
        val resumeButton = findViewById<Button>(R.id.resumeButton)

        downloadButton.setOnClickListener {
            DownloadManager.instance.download(url1, destination1, object : DownloadListener {
                override fun onStart() {
                    Log.d("Download", "Started 1")
                }

                override fun onProgress(downloaded: Long, total: Long) {
                    val progress = if (total > 0) (downloaded * 100 / total).toInt() else 0
                    if(progress %10 ==0){
                        Log.d("Download", "Progress1: $progress%")
                    }
                }

                override fun onComplete() {
                    Log.d("Download", "Completed 1")
                }

                override fun onError(exception: Exception) {
                    Log.e("Download", "Error 1: ${exception.message}")
                }
            }, Priority.HIGH)

            DownloadManager.instance.download(url2, destination2, object : DownloadListener {
                override fun onStart() {
                    Log.d("Download", "Started 2")
                }

                override fun onProgress(downloaded: Long, total: Long) {
                    val progress = if (total > 0) (downloaded * 100 / total).toInt() else 0
                    if(progress %10 ==0){
                        Log.d("Download", "Progress2: $progress%")
                    }
                }

                override fun onComplete() {
                    Log.d("Download", "Completed 2")
                }

                override fun onError(exception: Exception) {
                    Log.e("Download", "Error2: ${exception.message}")
                }
            }, Priority.LOW)

            DownloadManager.instance.download(url3, destination3, object : DownloadListener {
                override fun onStart() {
                    Log.d("Download", "Started3")
                }

                override fun onProgress(downloaded: Long, total: Long) {
                    val progress = if (total > 0) (downloaded * 100 / total).toInt() else 0
                    if(progress %10 ==0){
                        Log.d("Download", "Progress3: $progress%")
                    }
                }

                override fun onComplete() {
                    Log.d("Download", "Completed3")
                }

                override fun onError(exception: Exception) {
                    Log.e("Download", "Error3: ${exception.message}")
                }
            }, Priority.MEDIUM)

            DownloadManager.instance.download(url4, destination4, object : DownloadListener {
                override fun onStart() {
                    Log.d("Download", "Started4")
                }

                override fun onProgress(downloaded: Long, total: Long) {
                    val progress = if (total > 0) (downloaded * 100 / total).toInt() else 0
                    if(progress %10 ==0){
                        Log.d("Download", "Progress4: $progress%")
                    }
                }

                override fun onComplete() {
                    Log.d("Download", "Completed4")
                }

                override fun onError(exception: Exception) {
                    Log.e("Download", "Error4: ${exception.message}")
                }
            }, Priority.HIGH)
        }

        pauseButton.setOnClickListener {
            DownloadManager.instance.pauseDownload(url1)
            DownloadManager.instance.pauseDownload(url2)
            DownloadManager.instance.pauseDownload(url3)
            DownloadManager.instance.pauseDownload(url4)
        }

        resumeButton.setOnClickListener {
            DownloadManager.instance.resumeDownload(url1)
            DownloadManager.instance.resumeDownload(url2)
            DownloadManager.instance.resumeDownload(url3)
            DownloadManager.instance.resumeDownload(url4)
        }
    }
}
