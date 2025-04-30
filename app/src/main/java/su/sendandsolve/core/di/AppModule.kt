package su.sendandsolve.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.utils.CurrentUser
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCurrentUser() : CurrentUser{
        return CurrentUser()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : RoomAppDatabase {
        return RoomAppDatabase.getAppDataBase(context)
    }
}