package su.sendandsolve.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.utils.CurrentUser
import su.sendandsolve.core.utils.SessionManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCurrentUser(sessionManager: SessionManager) : CurrentUser{
        return CurrentUser(sessionManager)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : RoomAppDatabase {
        return RoomAppDatabase.getAppDataBase(context)
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context) : SessionManager {
        return SessionManager(context)
    }
}