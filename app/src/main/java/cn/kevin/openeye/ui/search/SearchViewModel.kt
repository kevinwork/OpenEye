package cn.kevin.openeye.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import cn.kevin.openeye.logic.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchViewModel @ViewModelInject constructor(repository: SearchRepository): ViewModel() {
    //当前缓存的数据
    var dataList = ArrayList<String>()

    val data: Flow<List<String>> = repository.getHostSearch()
}