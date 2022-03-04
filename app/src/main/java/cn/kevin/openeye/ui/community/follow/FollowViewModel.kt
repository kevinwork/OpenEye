package cn.kevin.openeye.ui.community.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cn.kevin.openeye.logic.model.Follow
import cn.kevin.openeye.logic.repository.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
    val repository: CommunityRepository
) : ViewModel() {

    var dataList = ArrayList<Follow.Item>()

    fun getPagingData(): Flow<PagingData<Follow.Item>> {
        return repository.getFollowPagingData().cachedIn(viewModelScope)
    }
}