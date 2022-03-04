package cn.kevin.openeye.logic.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import cn.kevin.openeye.Const
import cn.kevin.openeye.logic.api.NotificationService
import cn.kevin.openeye.logic.model.PushMessage
import cn.kevin.openeye.ui.notification.push.PushPagingSource
import kotlinx.coroutines.flow.Flow

class NotificationRepositoryImpl(
    private val notificationService: NotificationService
) : NotificationRepository {

    override fun getMessagePagingData(): Flow<PagingData<PushMessage.Message>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = { PushPagingSource(notificationService) }
        ).flow
    }
}