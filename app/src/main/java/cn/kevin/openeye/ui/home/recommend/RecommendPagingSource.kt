package cn.kevin.openeye.ui.home.recommend

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.kevin.openeye.logic.api.HomePageService
import cn.kevin.openeye.logic.model.Discovery
import cn.kevin.openeye.logic.model.HomePageRecommend

class RecommendPagingSource(private val homePageService: HomePageService) : PagingSource<String, HomePageRecommend.Item>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, HomePageRecommend.Item> {
        return try {
            val page = params.key ?:  HomePageService.HOMEPAGE_RECOMMEND_URL
            //val pageSize = params.loadSize
            val repoResponse = homePageService.getHomePageRecommend(page)
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

    override fun getRefreshKey(state: PagingState<String, HomePageRecommend.Item>): String? = null
}