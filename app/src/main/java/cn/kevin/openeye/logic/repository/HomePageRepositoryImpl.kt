package cn.kevin.openeye.logic.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import cn.kevin.openeye.Const
import cn.kevin.openeye.logic.api.HomePageService
import cn.kevin.openeye.logic.model.Daily
import cn.kevin.openeye.logic.model.Discovery
import cn.kevin.openeye.logic.model.HomePageRecommend
import cn.kevin.openeye.ui.home.daily.DailyPagingSource
import cn.kevin.openeye.ui.home.discovery.DiscoveryPagingSource
import cn.kevin.openeye.ui.home.recommend.RecommendPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomePageRepositoryImpl @Inject constructor(val homePageService: HomePageService) : HomePageRepository {

    override fun getDiscoveryPagingData(): Flow<PagingData<Discovery.Item>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = { DiscoveryPagingSource(homePageService) }
        ).flow.flowOn(Dispatchers.IO)
    }

    override fun getHomePageRecommendPagingData(): Flow<PagingData<HomePageRecommend.Item>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = { RecommendPagingSource(homePageService) }
        ).flow.flowOn(Dispatchers.IO)
    }

    override fun getDailyPagingData(): Flow<PagingData<Daily.Item>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = { DailyPagingSource(homePageService) }
        ).flow.flowOn(Dispatchers.IO)
    }
}