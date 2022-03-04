package cn.kevin.openeye.logic.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import cn.kevin.openeye.Const
import cn.kevin.openeye.logic.api.CommunityService
import cn.kevin.openeye.logic.model.CommunityRecommend
import cn.kevin.openeye.logic.model.Follow
import cn.kevin.openeye.ui.community.follow.FollowPagingSource
import cn.kevin.openeye.ui.community.recommend.RecommendPagingSource
import kotlinx.coroutines.flow.Flow

class CommunityRepositoryImpl constructor(
    private val communityService: CommunityService
) : CommunityRepository {

    override fun getCommunityRecommendPagingData(): Flow<PagingData<CommunityRecommend.Item>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = { RecommendPagingSource(communityService) }
        ).flow
    }

    override fun getFollowPagingData(): Flow<PagingData<Follow.Item>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = { FollowPagingSource(communityService) }
        ).flow
    }
}