package cn.kevin.openeye.ui.common.ui

import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import cn.kevin.openeye.R

import com.gyf.immersionbar.ktx.immersionBar

open class BaseActivity : AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setStatusBarBackground(R.color.colorPrimaryDark)
    }

    override fun setContentView(layoutView: View) {
        super.setContentView(layoutView)
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