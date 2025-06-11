package su.sendandsolve.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import su.sendandsolve.core.database.room.DatabasePassphrase
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
    fun provideDatabase(@ApplicationContext context: Context, supportFactory: SupportFactory) : RoomAppDatabase {
//        return RoomAppDatabase.getAppDataBase(context, "sendandsolve.db", supportFactory)
        return Room.inMemoryDatabaseBuilder(context, RoomAppDatabase::class.java)
            .openHelperFactory(supportFactory)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context) : SessionManager {
        return SessionManager(context)
    }

    @Provides
    @Singleton
    fun provideDatabasePassphrase(@ApplicationContext context: Context) : DatabasePassphrase {
        return DatabasePassphrase(context)
    }

    @Provides
    @Singleton
    fun provideSupportFactory(databasePassphrase: DatabasePassphrase) : SupportFactory {
        return SupportFactory(databasePassphrase.getPassphrase())
    }
}