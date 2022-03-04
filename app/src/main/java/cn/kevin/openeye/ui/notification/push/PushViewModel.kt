package cn.kevin.openeye.ui.notification.push

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cn.kevin.openeye.logic.model.PushMessage
import cn.kevin.openeye.logic.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PushViewModel @Inject constructor(
    val repository: NotificationRepository
) : ViewModel() {

    var dataList = ArrayList<PushMessage.Message>()

    fun getPagingData(): Flow<PagingData<PushMessage.Message>> {
        return repository.getMessagePagingData().cachedIn(viewModelScope)
    }
}
