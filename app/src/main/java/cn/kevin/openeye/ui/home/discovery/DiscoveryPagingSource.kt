package cn.kevin.openeye.ui.home.discovery

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.kevin.openeye.logic.api.HomePageService
import cn.kevin.openeye.logic.model.Discovery

class DiscoveryPagingSource(private val homePageService: HomePageService) : PagingSource<String, Discovery.Item>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Discovery.Item> {
        return try {
            val page = params.key ?:  HomePageService.DISCOVERY_URL
            //val pageSize = params.loadSize
            val repoResponse = homePageService.getDiscovery(page)
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


       /* return try {
            val page = params.key ?: HomePageService.DISCOVERY_URL
            val repoResponse = homePageService.getDiscovery(page)
            val repoItems = repoResponse.itemList
            val prevKey = null
            val nextKey = if (repoItems.isNotEmpty() && !repoResponse.nextPageUrl.isNullOrEmpty()) repoResponse.nextPageUrl else null
            LoadResult.Page(repoItems, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }*/
    }

    override fun getRefreshKey(state: PagingState<String, Discovery.Item>): String? = null
}