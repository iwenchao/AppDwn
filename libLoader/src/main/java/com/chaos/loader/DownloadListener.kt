package com.chaos.loader

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2024/6/5 16:03.
 * @Description :描述
 */
interface DownloadListener {
    fun onStart()
    fun onProgress(downloaded: Long, total: Long)
    fun onComplete()
    fun onError(exception: Exception)
    fun onStatusChange(status: DownloadStatus)
}