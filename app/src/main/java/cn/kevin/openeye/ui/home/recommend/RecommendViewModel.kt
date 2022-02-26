package cn.kevin.openeye.ui.home.recommend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cn.kevin.openeye.logic.model.Discovery
import cn.kevin.openeye.logic.model.HomePageRecommend
import cn.kevin.openeye.logic.repository.HomePageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(val repository: HomePageRepository): ViewModel() {

    fun getPagingData(): Flow<PagingData<HomePageRecommend.Item>> {
        return repository.getHomePageRecommendPagingData().cachedIn(viewModelScope)
    }

}