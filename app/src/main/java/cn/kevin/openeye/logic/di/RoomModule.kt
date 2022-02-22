package cn.kevin.openeye.logic.di


import android.app.Application
import androidx.room.Room
import cn.kevin.openeye.logic.db.AppDatabase
import cn.kevin.openeye.logic.db.SearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideAppDataBase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "video_collections.db").build()
    }

    @Singleton
    @Provides
    fun provideSearchDao(database: AppDatabase): SearchDao {
        return database.searchDao()
    }

}