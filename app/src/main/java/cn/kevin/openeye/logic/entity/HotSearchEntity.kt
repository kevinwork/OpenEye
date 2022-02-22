package cn.kevin.openeye.logic.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HotSearchEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val keywords:String
)
