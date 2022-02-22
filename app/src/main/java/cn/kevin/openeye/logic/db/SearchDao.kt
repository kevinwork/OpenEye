package cn.kevin.openeye.logic.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cn.kevin.openeye.logic.entity.HotSearchEntity

@Dao
interface SearchDao {

    @Query("DELETE FROM HotSearchEntity")
    fun clearHotSearch()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun cacheHotSearch(bean: List<HotSearchEntity>?)
}