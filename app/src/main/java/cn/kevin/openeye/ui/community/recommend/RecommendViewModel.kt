package cn.kevin.openeye.ui.community.recommend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cn.kevin.openeye.logic.model.CommunityRecommend
import cn.kevin.openeye.logic.repository.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(
    val repository: CommunityRepository
) : ViewModel() {

    var dataList = ArrayList<CommunityRecommend.Item>()

    fun getPagingData(): Flow<PagingData<CommunityRecommend.Item>> {
        return repository.getCommunityRecommendPagingData().cachedIn(viewModelScope)
    }
}