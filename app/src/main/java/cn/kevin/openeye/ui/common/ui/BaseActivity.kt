package cn.kevin.openeye.ui.common.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import cn.kevin.openeye.R
import cn.kevin.openeye.extension.logD
import cn.kevin.openeye.extension.logV
import com.gyf.immersionbar.ImmersionBar

import com.gyf.immersionbar.ktx.immersionBar
import com.gyf.immersionbar.ktx.isSupportStatusBarDarkFont

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        /*immersionBar {
            statusBarColor(R.color.colorPrimaryDark)
            navigationBarColor(R.color.colorPrimaryDark)
            //statusBarDarkFont(true, 0.4f)
        }*/

        setStatusBarBackground(R.color.colorPrimaryDark)


    }

    override fun setContentView(layoutView: View) {
        super.setContentView(layoutView)
       /* immersionBar {
            titleBar(layoutView)
            statusBarColor(R.color.colorPrimaryDark)
            navigationBarColor(R.color.colorPrimaryDark)
            //statusBarDarkFont(true, 0.4f)
        }*/
        setStatusBarBackground(R.color.colorPrimaryDark)
    }

    /**
     * 设置状态栏背景色
     */
    open fun setStatusBarBackground(@ColorRes statusBarColor: Int) {
        immersionBar {
            // 自动状态栏字体变色，必须指定状态栏颜色才可以自动变色
            autoStatusBarDarkModeEnable(true, 0.2f)
            // 状态栏颜色，不写默认透明色
            statusBarColor(statusBarColor)
            // 解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
            fitsSystemWindows(true)
        }
    }

}