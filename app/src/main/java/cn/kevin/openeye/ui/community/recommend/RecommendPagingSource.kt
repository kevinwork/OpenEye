package cn.kevin.openeye.ui.community.recommend

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.kevin.openeye.logic.api.CommunityService
import cn.kevin.openeye.logic.model.CommunityRecommend

class RecommendPagingSource(private val communityService: CommunityService) : PagingSource<String, CommunityRecommend.Item>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, CommunityRecommend.Item> {
        return try {
            val page = params.key ?: CommunityService.COMMUNITY_RECOMMEND_URL
            val repoResponse = communityService.getCommunityRecommend(page)
            val repoItems = repoResponse.itemList
            val prevKey = null
            val nextKey = if (repoItems.isNotEmpty() && !repoResponse.nextPageUrl.isNullOrEmpty()) repoResponse.nextPageUrl else null
            LoadResult.Page(repoItems, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, CommunityRecommend.Item>): String? = null
}