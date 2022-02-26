package cn.kevin.openeye.ui.home.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cn.kevin.openeye.logic.model.Daily
import cn.kevin.openeye.logic.model.Discovery
import cn.kevin.openeye.logic.repository.HomePageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(val repository: HomePageRepository): ViewModel() {

    fun getPagingData(): Flow<PagingData<Daily.Item>> {
        return repository.getDailyPagingData().cachedIn(viewModelScope)
    }

}