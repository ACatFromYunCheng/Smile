package com.yhao.model.service

import com.google.gson.Gson
import com.yhao.commen.Const.Companion.buildUrl
import com.yhao.model.bean.Huaban
import com.yhao.model.bean.HuabanResult
import java.net.URL
import java.util.*

/**
 * Created by yhao on 17-9-5.
 *
 */

class HuabanService {
    companion object {
        val baseUrl = "http://route.showapi.com/819-1"

        fun buildBaseUrl(type: Int, page: Int, num: Int): String {
            return buildUrl("$baseUrl?type=$type&num=$num&page=$page")
        }

        fun getData(type: Int, page: Int, num: Int = 20): MutableList<Huaban>? {
            var forecastJsonStr: String? = null
            try {
                forecastJsonStr = URL(buildBaseUrl(type, page, num)).readText()
            } catch (e: Exception) {
                return null
            }
            val data = Gson().fromJson(forecastJsonStr, HuabanResult::class.java)
            val iterator = data.showapi_res_body.entrySet().iterator()
            val huabans: MutableList<Huaban> = ArrayList()
            while (iterator.hasNext()) {
                val element = iterator.next()
                try {
                    val huaban = Gson().fromJson(element.value, Huaban::class.java)
                    huabans.add(huaban)
                } catch (e: Exception) {
                }
            }
            return if (huabans.size > 0) huabans else null
        }
    }
}
