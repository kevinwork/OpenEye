package cn.kevin.openeye.logic.di

import cn.kevin.openeye.logic.api.SearchService
import cn.kevin.openeye.logic.db.SearchDao
import cn.kevin.openeye.logic.repository.SearchRepository
import cn.kevin.openeye.logic.repository.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton

@InstallIn(ActivityComponent::class)
@Module
object RepositoryModule {

    @ActivityScoped
    @Provides
    fun provideSearchRepository(api: SearchService, dao: SearchDao): SearchRepository {
        return SearchRepositoryImpl(api, dao)
    }


}