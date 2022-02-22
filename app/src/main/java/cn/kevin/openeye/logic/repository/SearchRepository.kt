package cn.kevin.openeye.logic.repository

import dagger.Binds
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getHostSearch():Flow<List<String>>

}