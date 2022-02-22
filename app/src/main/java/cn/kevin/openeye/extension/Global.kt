package cn.kevin.openeye.extension

import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cn.kevin.openeye.OpenEyeApplication


/**
 * 获取DataStore实例。
 */
val dataStore: DataStore<Preferences> = OpenEyeApplication.context.dataStore

/**
 * 批量设置控件点击事件。
 *
 * @param v 点击的控件
 * @param block 处理点击事件回调代码块
 */
fun setOnClickListener(vararg v: View?, block: View.() -> Unit) {
    val listener = View.OnClickListener { it.block()}
    v.forEach { it?.setOnClickListener(listener) }
}