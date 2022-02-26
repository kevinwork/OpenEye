package cn.kevin.openeye.ui.home.daily

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.kevin.openeye.logic.api.HomePageService
import cn.kevin.openeye.logic.model.Daily
import cn.kevin.openeye.logic.model.Discovery

class DailyPagingSource(private val homePageService: HomePageService) : PagingSource<String, Daily.Item>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Daily.Item> {
        return try {
            val page = params.key ?:  HomePageService.DAILY_URL
            //val pageSize = params.loadSize
            val repoResponse = homePageService.getDaily(page)
            //val test = Gson().toJson(repoResponse)
            //logD("zwz DiscoveryPagingSource:", test)
            val repoItems = repoResponse.itemList
            //val prevKey = if (page > 1) page - 1 else null
            val prevKey = null
            val nextKey = if (repoItems.isNotEmpty() && !repoResponse.nextPageUrl.isNullOrEmpty()) repoResponse.nextPageUrl else null
            return LoadResult.Page(repoItems, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<String, Daily.Item>): String? = null
}