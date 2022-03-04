package cn.kevin.openeye.logic.api

import cn.kevin.openeye.logic.di.NetworkModule
import cn.kevin.openeye.logic.model.CommunityRecommend
import cn.kevin.openeye.logic.model.Follow
import retrofit2.http.GET
import retrofit2.http.Url

interface CommunityService {

    @GET
    suspend fun getCommunityRecommend(@Url url: String): CommunityRecommend

    @GET
    suspend fun getFollow(@Url url: String): Follow

    companion object {
        /**
         * 社区-推荐列表
         */
       const val COMMUNITY_RECOMMEND_URL = "${NetworkModule.BASE_URL}api/v7/community/tab/rec"


        /**
         * 社区-关注列表
         */
        const val FOLLOW_URL = "${NetworkModule.BASE_URL}api/v6/community/tab/follow"
    }
}