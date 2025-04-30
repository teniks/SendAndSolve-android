package su.sendandsolve.core.database.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.database.room.entity.NoteTask
import su.sendandsolve.core.database.room.entity.TaskHierarchy
import su.sendandsolve.core.database.room.entity.TaskResource
import su.sendandsolve.core.database.room.entity.TaskTag
import su.sendandsolve.core.database.room.mapper.TaskMapper
import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.Task
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val db: RoomAppDatabase
) : Repository<Task> {
    override suspend fun getById(id: UUID): Task? {
        return db.taskDao().getById(id)?.let { TaskMapper.toDomain(it) }
    }

    override fun getAll(isDeleted: Boolean): Flow<List<Task>> {
        return db.taskDao().getDeleted(isDeleted).map { it.map(TaskMapper::toDomain)}
    }

    override suspend fun delete(domain: Task) {
        db.taskDao().setDeleted(domain.uuid)
    }

    override suspend fun update(domain: Task) {
        db.taskDao().update(TaskMapper.toEntity(domain))
    }

    override suspend fun insert(domain: Task) {
        db.taskDao().insert(TaskMapper.toEntity(domain))
    }

    suspend fun updateTasks(domain: Task) {
        domain.run {
            childTasks.forEach { note ->
                when (note.value) {
                    DomainState.Insert -> {
                        db.taskHierarchyDao().insert(
                            TaskHierarchy(
                                parentId = this.uuid,
                                childId = note.key.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )
                    }

                    DomainState.Delete -> {
                        db.taskHierarchyDao().setDeleted(this.uuid, note.key.uuid)
                        db.taskHierarchyDao().setSynced(this.uuid, note.key.uuid, false)
                        note.key.isDeleted = true
                        note.key.isSynced = false

                    }

                    DomainState.Recover -> {
                        db.taskHierarchyDao().setDeleted(this.uuid, note.key.uuid)
                        db.taskHierarchyDao().setSynced(this.uuid, note.key.uuid, false)
                        note.key.isDeleted = true
                        note.key.isSynced = false
                    }

                    DomainState.Read -> {}
                }
            }
        }
        domain.run {
            parentTasks.forEach { note ->
                when (note.value) {
                    DomainState.Insert -> {
                        db.taskHierarchyDao().insert(
                            TaskHierarchy(
                                parentId = note.key.uuid,
                                childId = this.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )
                    }

                    DomainState.Delete -> {
                        db.taskHierarchyDao().setDeleted(note.key.uuid, this.uuid)
                        db.taskHierarchyDao().setSynced(note.key.uuid, this.uuid, false)
                        note.key.isDeleted = true
                        note.key.isSynced = false

                    }

                    DomainState.Recover -> {
                        db.taskHierarchyDao().setDeleted(note.key.uuid, this.uuid, false)
                        db.taskHierarchyDao().setSynced(note.key.uuid, this.uuid, false)
                        note.key.isDeleted = true
                        note.key.isSynced = false
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }

    suspend fun updateTags(domain: Task) {
        domain.run {
            tags.forEach { tag ->
                when (tag.value) {
                    DomainState.Insert -> {
                        db.taskTagDao().insert(
                            TaskTag(
                                taskId = this.uuid,
                                tagId = tag.key.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )
                    }

                    DomainState.Delete -> {
                        db.taskTagDao().setDeleted(this.uuid, tag.key.uuid)
                        db.taskTagDao().setSynced(this.uuid, tag.key.uuid, false)
                        tag.key.isDeleted = true
                        tag.key.isSynced = false
                        tags[tag.key] = DomainState.Read
                    }

                    DomainState.Recover -> {
                        db.taskTagDao().setDeleted(this.uuid, tag.key.uuid, false)
                        db.taskTagDao().setSynced(this.uuid, tag.key.uuid, false)
                        tag.key.isDeleted = true
                        tag.key.isSynced = false
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }

    suspend fun updateResources(domain: Task) {
        domain.run {
            resources.forEach { resource ->
                when (resource.value) {
                    DomainState.Insert -> {
                        db.taskResourceDao().insert(
                            TaskResource(
                                resourceId = resource.key.uuid,
                                taskId = this.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )
                        resources[resource.key] = DomainState.Read
                    }

                    DomainState.Delete -> {
                        db.taskResourceDao().setDeleted(resource.key.uuid, this.uuid)
                        db.taskResourceDao().setSynced(resource.key.uuid, this.uuid, false)
                        resource.key.isDeleted = true
                        resource.key.isSynced = false
                        resources[resource.key] = DomainState.Read
                    }

                    DomainState.Recover -> {
                        db.taskResourceDao().setDeleted(resource.key.uuid, this.uuid, false)
                        db.taskResourceDao().setSynced(resource.key.uuid, this.uuid, false)
                        resource.key.isDeleted = true
                        resource.key.isSynced = false
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }

    suspend fun updateNotes(domain: Task) {
        domain.run {
            notes.forEach { note ->
                when (note.value) {
                    DomainState.Insert -> {
                        db.noteTaskDao().insert(
                            NoteTask(
                                noteId = note.key.uuid,
                                taskId = this.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )
                        notes[note.key] = DomainState.Read
                    }

                    DomainState.Delete -> {
                        db.noteTaskDao().setDeleted(note.key.uuid, this.uuid)
                        db.noteTaskDao().setSynced(note.key.uuid, this.uuid, false)
                        note.key.isDeleted = true
                        note.key.isSynced = false
                        notes[note.key] = DomainState.Read
                    }

                    DomainState.Recover -> {
                        db.noteTaskDao().setDeleted(note.key.uuid, this.uuid, false)
                        db.noteTaskDao().setSynced(note.key.uuid, this.uuid, false)
                        note.key.isDeleted = true
                        note.key.isSynced = false
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }
}