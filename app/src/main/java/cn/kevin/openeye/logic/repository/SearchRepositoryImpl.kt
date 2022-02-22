package cn.kevin.openeye.logic.repository

import cn.kevin.openeye.logic.api.SearchService
import cn.kevin.openeye.logic.db.SearchDao
import cn.kevin.openeye.logic.entity.HotSearchEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking

class SearchRepositoryImpl(
    val api:SearchService,
    val dao:SearchDao
) : SearchRepository{
    override fun getHostSearch(): Flow<List<String>> = flow {

        val response = api.getHotSearch()
        val hostSearches : List<HotSearchEntity> = response.map {
            HotSearchEntity(keywords = it)
        }
        dao.clearHotSearch()
        dao.cacheHotSearch(hostSearches)
        emit(response)
    }.flowOn(Dispatchers.IO)

}