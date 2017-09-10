package com.yhao.commen.download

import com.yhao.commen.Const.Companion.ROOT_DIR
import com.yhao.commen.util.MD5Util
import okhttp3.*
import okio.Okio
import java.io.File
import java.io.IOException

/**
 * Created by yhao on 17-9-7.
 *
 */

object ProgressDownload {

    private var progressListener: ProgressListener? = null


    private val mClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addNetworkInterceptor(ProgressInterceptor(listener)).build()
    }


    private val listener: ProgressListener = object : ProgressListener {
        override fun onProgress(readByte: Long, totalByte: Long, done: Boolean) {
            if (progressListener != null) {
                progressListener!!.onProgress(readByte, totalByte, done)
            }
        }

        override fun onSave(filePath: String) {}
    }

    fun downloadPhoto(url: String, progressListener: ProgressListener) {
        val existFilePath: String? = exist(url)
        if (existFilePath != null) {
            progressListener.onSave(existFilePath)
            return
        }
        this.progressListener = progressListener
        val request = Request.Builder()
                .url(url)
                .build()
        mClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val filePath = ROOT_DIR + MD5Util.getHashKey(url)
                val file = File(filePath)
                val sink = Okio.buffer(Okio.sink(file))
                val source = response.body()!!.source()
                sink.writeAll(source)
                sink.flush()
                progressListener.onSave(filePath)
            }
        })
    }

    fun exist(url: String): String? {
        val filePath = ROOT_DIR + MD5Util.getHashKey(url)
        val file = File(filePath)
        return if (file.exists()) filePath else null
    }

}
