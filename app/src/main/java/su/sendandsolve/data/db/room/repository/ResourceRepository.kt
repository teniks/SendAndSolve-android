package su.sendandsolve.data.db.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.data.db.room.RoomAppDatabase
import su.sendandsolve.data.db.room.dao.ResourceDao
import su.sendandsolve.data.db.room.dao.TaskResourceDao
import su.sendandsolve.data.db.room.entity.TaskResource
import su.sendandsolve.data.db.room.mapper.ResourceMapper
import su.sendandsolve.data.domain.DomainState
import su.sendandsolve.data.domain.Repository
import su.sendandsolve.data.domain.model.Resource
import java.time.Instant
import java.util.UUID

class ResourceRepository(
    private val db: RoomAppDatabase
) : Repository<Resource> {
    override suspend fun getById(id: UUID): Resource? {
        return db.resourceDao().getById(id)?.let { ResourceMapper.toDomain(it) }
    }

    override fun getAll(isDeleted: Boolean): Flow<List<Resource>> {
        return db.resourceDao().getDeleted(isDeleted).map { list -> list.map(ResourceMapper::toDomain) }
    }

    override suspend fun delete(domain: Resource) {
        db.resourceDao().setDelete(domain.uuid)
    }

    override suspend fun update(domain: Resource) {
        db.resourceDao().update(ResourceMapper.toEntity(domain))
    }

    override suspend fun insert(domain: Resource) {
        db.resourceDao().insert(ResourceMapper.toEntity(domain))
    }

    suspend fun updateTasks(domain: Resource) {
        domain.run {
            tasks.forEach { task ->
                when(task.value){
                    DomainState.Insert -> {
                        db.taskResourceDao().insert(
                            TaskResource(
                                resourceId = domain.uuid,
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
                        db.taskResourceDao().setDeleted(task.key.uuid, domain.uuid)
                        task.key.isDeleted = true
                        task.key.isSynced = false
                        tasks[task.key] = DomainState.Read
                    }
                    DomainState.Read -> {  }
                }
            }
        }
    }
}