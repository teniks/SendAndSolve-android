package su.sendandsolve.features.tasks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.database.room.repository.DeviceRepository
import su.sendandsolve.core.database.room.repository.GroupRepository
import su.sendandsolve.core.database.room.repository.NoteRepository
import su.sendandsolve.core.database.room.repository.ResourceRepository
import su.sendandsolve.core.database.room.repository.SessionRepository
import su.sendandsolve.core.database.room.repository.TagRepository
import su.sendandsolve.core.database.room.repository.TaskRepository
import su.sendandsolve.core.database.room.repository.TeamRepository
import su.sendandsolve.core.database.room.repository.UserRepository
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.Device
import su.sendandsolve.features.tasks.domain.model.Group
import su.sendandsolve.features.tasks.domain.model.Note
import su.sendandsolve.features.tasks.domain.model.Resource
import su.sendandsolve.features.tasks.domain.model.Session
import su.sendandsolve.features.tasks.domain.model.Tag
import su.sendandsolve.features.tasks.domain.model.Task
import su.sendandsolve.features.tasks.domain.model.Team
import su.sendandsolve.features.tasks.domain.model.User

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideUserRepository(db: RoomAppDatabase): Repository<User>{
        return UserRepository(db)
    }

    @Provides
    fun provideTeamRepository(db: RoomAppDatabase): Repository<Team>{
        return TeamRepository(db)
    }

    @Provides
    fun provideTaskRepository(db: RoomAppDatabase): Repository<Task>{
        return TaskRepository(db)
    }

    @Provides
    fun provideTagRepository(db: RoomAppDatabase): Repository<Tag>{
        return TagRepository(db)
    }

    @Provides
    fun provideSessionRepository(db: RoomAppDatabase): Repository<Session>{
        return SessionRepository(db)
    }

    @Provides
    fun provideResourceRepository(db: RoomAppDatabase): Repository<Resource>{
        return ResourceRepository(db)
    }

    @Provides
    fun provideNoteRepository(db: RoomAppDatabase): Repository<Note>{
        return NoteRepository(db)
    }

    @Provides
    fun provideGroupRepository(db: RoomAppDatabase): Repository<Group>{
        return GroupRepository(db)
    }

    @Provides
    fun provideDeviceRepository(db: RoomAppDatabase): Repository<Device>{
        return DeviceRepository(db)
    }
}