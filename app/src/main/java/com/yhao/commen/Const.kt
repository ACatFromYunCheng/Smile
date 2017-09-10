package com.yhao.commen

import android.os.Environment
import java.io.File

/**
 * Created by yhao on 17-9-6.
 *
 */

class Const {
    companion object {

        private val yiyuan_appid = "45578"
        private val yiyuan_secret = "4e6e7a13e16a42059a75a9a8931a779f"
        private val yiyuanAuth = "&showapi_sign=$yiyuan_secret&showapi_appid=$yiyuan_appid"

        fun buildUrl(url: String): String {
            return "$url&$yiyuanAuth"
        }

        val ROOT_DIR: String by lazy {
            val path = Environment.getExternalStorageDirectory().absolutePath + "/ForYourSmile/"
            val file = File(path)
            if (!file.exists()) {
                file.mkdirs()
            }
            path
        }

    }
}