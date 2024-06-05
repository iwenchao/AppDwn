package com.chaos.loader

import okhttp3.OkHttpClient
import java.util.concurrent.Executors
import java.util.concurrent.PriorityBlockingQueue

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2024/6/5 15:59.
 * @Description :描述
 */
class DownloadManager private constructor() {
    private val maxConcurrentDownloads = 1
    private val executorService = Executors.newFixedThreadPool(maxConcurrentDownloads)
    private val client = OkHttpClient()
    private val taskQueue = PriorityBlockingQueue<DownloadTask>()
    private val taskMap = mutableMapOf<String, DownloadController>()
    @Volatile private var isRunning = false

    fun download(url: String, destination: String, listener: DownloadListener, priority: Priority = Priority.MEDIUM) {
        val downloadTask = DownloadTask(url, destination, client, listener, priority)
        val controller = DownloadController(downloadTask)
        taskMap[url] = controller
        taskQueue.add(downloadTask)
        startNext()
    }

    fun pauseDownload(url: String) {
        taskMap[url]?.pause()
    }

    fun resumeDownload(url: String) {
        taskMap[url]?.resume()
    }

    private fun startNext() {
        synchronized(this) {
            if (isRunning || taskQueue.isEmpty()) return

            val task = taskQueue.poll()
            executorService.submit {
                isRunning = true
                task.run()
                isRunning = false
                startNext()
            }
        }
    }

    companion object {
        val instance: DownloadManager by lazy { DownloadManager() }
    }
}