package cn.kevin.openeye.logic.di

import cn.kevin.openeye.logic.api.HomePageService
import cn.kevin.openeye.logic.api.SearchService
import cn.kevin.openeye.logic.api.VideoService
import cn.kevin.openeye.logic.db.SearchDao
import cn.kevin.openeye.logic.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideSearchRepository(api: SearchService, dao: SearchDao): SearchRepository {
        return SearchRepositoryImpl(api, dao)
    }

    @Singleton
    @Provides
    fun provideHomePageRepository(api: HomePageService): HomePageRepository {
        return HomePageRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideVideoRepository(api: VideoService): VideoRepository {
        return VideoRepositoryImpl(api)
    }



}