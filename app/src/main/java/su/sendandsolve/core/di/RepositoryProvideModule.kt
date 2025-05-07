package su.sendandsolve.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.database.room.repository.UserRepository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryProvideModule {

    @Provides
    fun provideUserRepository(db: RoomAppDatabase): UserRepository {
        return UserRepository(db)
    }
}