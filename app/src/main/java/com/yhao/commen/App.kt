package com.yhao.commen

import android.app.Application
import com.yhao.commen.notNullSingleValue

/**
 * Created by yhao on 17-9-4.
 *
 */

class App : Application() {


    companion object {
        var instance: App by notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}