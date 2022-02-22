package cn.kevin.openeye.logic.db

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.kevin.openeye.logic.entity.HotSearchEntity

@Database(
    entities =[HotSearchEntity::class],
    version = 1,
    //是否需要导出
    exportSchema =false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao
}