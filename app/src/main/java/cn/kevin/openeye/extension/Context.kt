package cn.kevin.openeye.extension

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import cn.kevin.openeye.OpenEyeApplication

/**
 * 获取DataStore实例
 */

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = OpenEyeApplication.context.packageName + "_preferences",
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, OpenEyeApplication.context.packageName + "_preferences"))
    })