package com.chaos.loader.utils

import java.io.File

/**
 * @Author      : wen
 * @Email       : iwenchaos6688@163.com
 * @Date        : on 2024/6/5 16:04.
 * @Description :描述
 */
object FileUtils {
    fun createFileIfNotExists(path: String) {
        val file = File(path)
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
        }
    }
}