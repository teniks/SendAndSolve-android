package su.sendandsolve.features.tasks.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
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
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserRepository(repository: UserRepository): Repository<User>

    @Binds
    abstract fun bindTeamRepository(repository: TeamRepository): Repository<Team>

    @Binds
    abstract fun bindTaskRepository(repository: TaskRepository): Repository<Task>

    @Binds
    abstract fun bindTagRepository(repository: TagRepository): Repository<Tag>

    @Binds
    abstract fun bindSessionRepository(repository: SessionRepository): Repository<Session>

    @Binds
    abstract fun bindResourceRepository(repository: ResourceRepository): Repository<Resource>

    @Binds
    abstract fun bindNoteRepository(repository: NoteRepository): Repository<Note>

    @Binds
    abstract fun bindGroupRepository(repository: GroupRepository): Repository<Group>

    @Binds
    abstract fun bindDeviceRepository(repository: DeviceRepository): Repository<Device>
}