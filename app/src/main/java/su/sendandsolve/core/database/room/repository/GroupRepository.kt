package su.sendandsolve.core.database.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.database.room.entity.TaskGroup
import su.sendandsolve.core.database.room.mapper.GroupMapper
import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.Group
import java.time.Instant
import java.util.UUID

class GroupRepository(
    private val db: RoomAppDatabase
) : Repository<Group> {
    override suspend fun getById(id: UUID): Group? {
        return db.groupDao().getById(id)?.let { GroupMapper.toDomain(it) }
    }

    override fun getAll(isDeleted: Boolean): Flow<List<Group>> {
        return db.groupDao().getDeleted(isDeleted).map { list -> list.map(GroupMapper::toDomain) }
    }

    override suspend fun delete(domain: Group) {
        db.groupDao().setDeleted(domain.uuid, true)
    }

    override suspend fun update(domain: Group) {
        db.groupDao().update(GroupMapper.toEntity(domain))
    }

    override suspend fun insert(domain: Group) {
        db.groupDao().insert(GroupMapper.toEntity(domain))
    }

    suspend fun updateTasks(domain: Group){
        domain.run {
            tasks.forEach { task ->
                when(task.value){
                    DomainState.Insert -> {
                        db.taskGroupDao().insert(
                            TaskGroup(
                                groupId = this.uuid,
                                taskId = task.key.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )
                        tasks[task.key] = DomainState.Read
                    }

                    DomainState.Delete -> {
                        db.taskGroupDao().setDeleted(groupId = this.uuid, taskId = task.key.uuid)
                        task.key.isDeleted = true
                        task.key.isSynced = false
                        tasks[task.key] = DomainState.Read
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }

}