package cn.kevin.openeye.logic.repository

import androidx.paging.PagingData
import cn.kevin.openeye.logic.model.Discovery
import kotlinx.coroutines.flow.Flow

interface HomePageRepository {

    fun getDiscoveryPagingData(): Flow<PagingData<Discovery.Item>>
}