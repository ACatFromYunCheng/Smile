package com.yhao.model.service

import com.google.gson.Gson
import com.yhao.commen.Const.Companion.buildUrl
import com.yhao.model.bean.*
import java.net.URL
import java.util.*

/**
 * Created by yhao on 17-9-5.
 *
 */

class JokeService {
    companion object {
        val baseUrl = "http://route.showapi.com/341-1"

        fun buildBaseUrl(page: Int, maxResult: Int): String {
            return buildUrl("$baseUrl?page=$page&maxResult=$maxResult")
        }

        fun getData(page: Int, maxResult: Int = 10): List<Joke>? {
            var forecastJsonStr: String? = null
            try {
                forecastJsonStr = URL(buildBaseUrl(page, maxResult)).readText()
            } catch (e: Exception) {
                return null
            }
            val data = Gson().fromJson(forecastJsonStr, JokeResult::class.java)
            val jokes: List<Joke> = data.showapi_res_body.contentlist
            return if (jokes.isNotEmpty()) jokes else null
        }
    }
}
