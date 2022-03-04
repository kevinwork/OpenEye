package cn.kevin.openeye.logic.repository

import androidx.paging.PagingData
import cn.kevin.openeye.logic.model.CommunityRecommend
import cn.kevin.openeye.logic.model.Follow
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {

    fun getCommunityRecommendPagingData(): Flow<PagingData<CommunityRecommend.Item>>

    fun getFollowPagingData(): Flow<PagingData<Follow.Item>>
}