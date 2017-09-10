package com.yhao.module

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat.*
import com.yhao.commen.download.ProgressDownload
import com.yhao.commen.download.ProgressListener
import kotlinx.android.synthetic.main.activity_boot.*
import pl.droidsonroids.gif.GifDrawable

class BootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boot)
        if ( checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            val str :Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE )
            requestPermissions(this,str, 1)
        }
        init()
    }

    private fun init() {

    }
}
