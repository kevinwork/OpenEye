package cn.kevin.openeye.ui.search


import androidx.lifecycle.ViewModel
import cn.kevin.openeye.logic.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//ViewModelInject deprecated
@HiltViewModel
class SearchViewModel @Inject constructor(repository: SearchRepository): ViewModel() {
    //当前缓存的数据
    var dataList = ArrayList<String>()

    val data: Flow<List<String>> = repository.getHostSearch()
}