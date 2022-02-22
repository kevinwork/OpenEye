package cn.kevin.openeye.logic.api;

import retrofit2.http.GET


interface SearchService {

    @GET("api/v3/queries/hot")
    suspend fun getHotSearch() : List<String>
}
