package cn.kevin.openeye.ui.notification.push

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.kevin.openeye.logic.api.NotificationService
import cn.kevin.openeye.logic.model.PushMessage

class PushPagingSource(private val notificationService: NotificationService) :
    PagingSource<String, PushMessage.Message>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, PushMessage.Message> {
        return try {
            val page = params.key ?: NotificationService.PUSHMESSAGE_URL
            val repoResponse = notificationService.getPushMessage(page)
            val repoItems = repoResponse.itemList
            val prevKey = null
            val nextKey =
                if (repoItems.isNotEmpty() && !repoResponse.nextPageUrl.isNullOrEmpty()) repoResponse.nextPageUrl else null
            LoadResult.Page(repoItems, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, PushMessage.Message>): String? = null
}