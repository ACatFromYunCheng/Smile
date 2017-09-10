package com.yhao.module

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.yhao.commen.Const.Companion.ROOT_DIR
import com.yhao.commen.preference
import com.yhao.commen.util.FileUtil
import com.yhao.commen.util.FileUtil.Companion.deleteFolderFile
import com.yhao.commen.util.FileUtil.Companion.getFolderSize
import com.yhao.commen.util.FileUtil.Companion.getPrintSize
import com.yhao.module.pic.GifFragment
import com.yhao.module.pic.PicFragment
import com.yhao.module.pic.TextFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import java.io.File
import kotlin.properties.Delegates


fun showSnackbar(viewGroup: ViewGroup, text: String, duration: Int = 1000) {
    val snack = Snackbar.make(viewGroup, text, duration)
    snack.view.setBackgroundColor(ContextCompat.getColor(viewGroup.context, R.color.colorPrimary))
    snack.show()
}

class MainActivity : AppCompatActivity() {

    val mFragments: Array<Fragment> = arrayOf(TextFragment(), PicFragment(), GifFragment())

    var mDefaultIndex: Int by preference(this@MainActivity, "sp_key_default_fragment", 0)

    var mCurrentIndex: Int = 0

    var mBackPressedTime by Delegates.observable(0L) {
        _, old, new ->
        if (new - old > 1000) {
            showSnackbar(coordinatorLayout, getString(R.string.exit_message))
        }
        if (new - old in 1..1000) {
            mDefaultIndex = mCurrentIndex
            finish()
        }
    }

    var mIsMenuOpen: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_text -> switchFragment(0, item.title.toString(), item)
                R.id.nav_pic -> switchFragment(1, item.title.toString(), item)
                R.id.nav_gif -> switchFragment(2, item.title.toString(), item)
                R.id.nav_clear -> clearCache(item)
                R.id.nav_about -> {
                    val intent = Intent(this, AboutAppActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.in_from_right, R.anim.stay)
                    true
                }
                else -> false
            }
        }
        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerClosed(drawerView: View?) {
                navigationView.menu.findItem(R.id.nav_clear).title = "清理缓存"
                mIsMenuOpen = false
            }

            override fun onDrawerOpened(drawerView: View?) {
                mIsMenuOpen = true
                doAsync {
                    val glideCacheDir = Glide.getPhotoCacheDir(this@MainActivity) as File
                    var totalSize: Long = getFolderSize(glideCacheDir)
                    val appRootDir = File(ROOT_DIR)
                    totalSize += getFolderSize(appRootDir)
                    uiThread {
                        navigationView.menu.findItem(R.id.nav_clear).title =
                                "清理缓存${getPrintSize(totalSize)}"
                    }
                }
            }
        })
        navigationView.setCheckedItem(R.id.nav_text)
        mCurrentIndex = mDefaultIndex
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, mFragments[mCurrentIndex])
                .commit()
        Glide.with(this).load(R.drawable.yhaolpz).into(navigationView.getHeaderView(0).find(R.id.avatar))
    }

    private fun clearCache(item: MenuItem): Boolean {
        item.title = "正在清理..."
        doAsync {
            val glideCacheDir = Glide.getPhotoCacheDir(this@MainActivity) as File
            var totalSize: Long = getFolderSize(glideCacheDir)
            val appRootDir = File(ROOT_DIR)
            totalSize += getFolderSize(appRootDir)
            uiThread {
                if (totalSize == 0L) {
                    item.title = "清理完成..."
                    return@uiThread
                }
                item.title = "正在清理${getPrintSize(totalSize)}..."
                doAsync {
                    deleteFolderFile(object : FileUtil.Companion.DeleteListener {
                        override fun onDelete(size: Long) {
                            uiThread {
                                totalSize -= size
                                item.title =
                                        if (totalSize == 0L) "清理完成"
                                        else "正在清理${getPrintSize(totalSize)}..."
                            }
                        }

                    }, glideCacheDir, appRootDir)
                }
            }
        }
        return true
    }

    private fun switchFragment(index: Int, title: String, item: MenuItem): Boolean {
        if (index != mCurrentIndex) {
            val trx = supportFragmentManager.beginTransaction()
            trx.hide(mFragments[mCurrentIndex])
            if (!mFragments[index].isAdded) {
                trx.add(R.id.content, mFragments[index])
            }
            trx.show(mFragments[index]).commit()
            item.isChecked = true
            toolbar.title = title
            mCurrentIndex = index
        }
        drawerLayout.closeDrawers()
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mBackPressedTime = if (mIsMenuOpen) mBackPressedTime else System.currentTimeMillis()
        }
        return true
    }


}


