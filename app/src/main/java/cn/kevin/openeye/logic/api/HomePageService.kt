package cn.kevin.openeye.logic.api;

import cn.kevin.openeye.logic.di.NetworkModule
import cn.kevin.openeye.logic.model.Discovery
import retrofit2.http.GET
import retrofit2.http.Url


interface HomePageService {

    @GET
    suspend fun getDiscovery(@Url url: String): Discovery

    companion object {

        /**
         * 首页-发现列表
         */
        const val DISCOVERY_URL = "${NetworkModule.BASE_URL}api/v7/index/tab/discovery"

        /**
         * 首页-推荐列表
         */
        const val HOMEPAGE_RECOMMEND_URL = "${NetworkModule.BASE_URL}api/v5/index/tab/allRec?page=0"

        /**
         * 首页-日报列表
         */
        const val DAILY_URL = "${NetworkModule.BASE_URL}api/v5/index/tab/feed"

        /**
         * 社区-推荐列表
         */
        const val COMMUNITY_RECOMMEND_URL = "${NetworkModule.BASE_URL}api/v7/community/tab/rec"

        /**
         * 社区-关注列表
         */
        const val FOLLOW_URL = "${NetworkModule.BASE_URL}api/v6/community/tab/follow"

        /**
         * 通知-推送列表
         */
        const val PUSHMESSAGE_URL = "${NetworkModule.BASE_URL}api/v3/messages"
    }
}
