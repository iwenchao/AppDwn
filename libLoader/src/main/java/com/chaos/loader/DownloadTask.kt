package com.chaos.loader

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2024/6/5 15:59.
 * @Description : 下载任务，一个下载文件对应一个任务
 */

class DownloadTask(
    val url: String,
    private val destination: String,
    private val client: OkHttpClient,
    private val listener: DownloadListener,
    val priority: Priority = Priority.MEDIUM
) : Runnable, Comparable<DownloadTask> {

    private val lock = ReentrantLock()
    private val pauseCondition = lock.newCondition()
    @Volatile private var paused = false
    @Volatile private var cancelled = false
    @Volatile private var status: DownloadStatus = DownloadStatus.PENDING

    fun pause() {
        lock.withLock {
            paused = true
            status = DownloadStatus.PAUSED
            listener.onStatusChange(status)
        }
    }

    fun resume() {
        lock.withLock {
            paused = false
            status = DownloadStatus.RESUMED
            listener.onStatusChange(status)
            pauseCondition.signal()
        }
    }

    override fun run() {
        var downloadedBytes = 0L
        val file = File(destination)

        try {
            if (!file.exists()) {
                file.parentFile?.mkdirs()
                file.createNewFile()
            } else {
                downloadedBytes = file.length()
            }

            status = DownloadStatus.STARTED
            listener.onStatusChange(status)

            val request = Request.Builder()
                .url(url)
                .header("Range", "bytes=$downloadedBytes-")
                .build()

            val response = client.newCall(request).execute()

            if (response.code == 416) {
                status = DownloadStatus.COMPLETED
                listener.onStatusChange(status)
                listener.onComplete()
                return
            }

            if (!response.isSuccessful) throw DownloadException("Failed to download file: ${response.message}")

            val inputStream: InputStream? = response.body?.byteStream()
            val outputStream = RandomAccessFile(file, "rw")
            outputStream.seek(downloadedBytes)

            inputStream.use { input ->
                outputStream.use { output ->
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    var totalBytesRead = downloadedBytes
                    val fileSize = response.body?.contentLength()?.let { it + downloadedBytes } ?: -1

                    while (input?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                        lock.withLock {
                            while (paused) {
                                pauseCondition.await()
                            }
                            if (cancelled) {
                                status = DownloadStatus.CANCELLED
                                listener.onStatusChange(status)
                                return
                            }
                        }
                        output.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead
                        listener.onProgress(totalBytesRead, fileSize)
                    }

                    status = DownloadStatus.COMPLETED
                    listener.onStatusChange(status)
                    listener.onComplete()
                }
            }
        } catch (e: Exception) {
            status = DownloadStatus.FAILED
            listener.onStatusChange(status)
            listener.onError(e)
        }
    }

    override fun compareTo(other: DownloadTask): Int {
        return other.priority.ordinal - this.priority.ordinal
    }
}