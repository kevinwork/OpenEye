package cn.kevin.openeye.logic.repository

import androidx.paging.PagingData
import cn.kevin.openeye.logic.model.PushMessage
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    fun getMessagePagingData(): Flow<PagingData<PushMessage.Message>>
}