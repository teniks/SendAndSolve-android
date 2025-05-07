package su.sendandsolve.features.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import su.sendandsolve.core.database.room.repository.UserRepository
import su.sendandsolve.core.utils.CurrentUser
import su.sendandsolve.features.auth.domain.repository.AuthRepository

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {

    @Provides
    fun provideAuthRepository(currentUser: CurrentUser, userRepository: UserRepository): AuthRepository {
        return AuthRepository(userRepository, currentUser)
    }
}