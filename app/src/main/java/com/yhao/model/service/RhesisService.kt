package com.yhao.model.service

import com.google.gson.Gson
import com.yhao.commen.Const.Companion.buildUrl
import com.yhao.model.bean.Rhesis
import com.yhao.model.bean.RhesisResult
import java.net.URL

/**
 * Created by yhao on 17-9-5.
 *
 */

class RhesisService {
    companion object {
        val baseUrl = "http://route.showapi.com/1211-1"

        fun buildBaseUrl(count: Int): String {
            return buildUrl("$baseUrl?count=$count")
        }

        fun getData(count: Int = 10): List<Rhesis>? {
            val forecastJsonStr: String?
            try {
                forecastJsonStr = URL(buildBaseUrl(count)).readText()
            } catch (e: Exception) {
                return null
            }
            val data = Gson().fromJson(forecastJsonStr, RhesisResult::class.java)
            val texts: List<Rhesis> = data.showapi_res_body.data
            return if (texts.isNotEmpty()) texts else null
        }
    }
}
