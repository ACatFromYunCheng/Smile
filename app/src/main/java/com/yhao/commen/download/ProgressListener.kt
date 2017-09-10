package com.yhao.commen.download

/**
 * Created by yhao on 17-9-7.
 *
 */
interface ProgressListener {
    fun onProgress(readByte: Long, totalByte: Long, done: Boolean)

    fun onSave(filePath: String)
}
