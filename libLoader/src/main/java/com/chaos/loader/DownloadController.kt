package com.chaos.loader

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2024/6/5 16:03.
 * @Description :描述
 */
class DownloadController(private val downloadTask: DownloadTask) {
    fun pause() {
        downloadTask.pause()
    }

    fun resume() {
        downloadTask.resume()
    }
}