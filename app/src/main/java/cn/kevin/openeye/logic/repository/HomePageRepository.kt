package cn.kevin.openeye.logic.repository

import androidx.paging.PagingData
import cn.kevin.openeye.logic.model.Daily
import cn.kevin.openeye.logic.model.Discovery
import cn.kevin.openeye.logic.model.HomePageRecommend
import kotlinx.coroutines.flow.Flow

interface HomePageRepository {

    fun getDiscoveryPagingData(): Flow<PagingData<Discovery.Item>>

    fun getHomePageRecommendPagingData(): Flow<PagingData<HomePageRecommend.Item>>

    fun getDailyPagingData(): Flow<PagingData<Daily.Item>>
}