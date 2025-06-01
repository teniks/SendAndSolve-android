package su.sendandsolve.core.database.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.database.room.entity.NoteTask
import su.sendandsolve.core.database.room.entity.TaskAssignment
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
            childTasks.forEach { child ->
                when (child.value) {
                    DomainState.Insert -> {
                        db.taskHierarchyDao().insert(
                            TaskHierarchy(
                                parentId = this.uuid,
                                childId = child.key.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )

                        domain.setReadChildTask(child.key)
                    }

                    DomainState.Delete -> {
                        db.taskHierarchyDao().setDeleted(this.uuid, child.key.uuid)
                        db.taskHierarchyDao().setSynced(this.uuid, child.key.uuid, false)
                        child.key.isDeleted = true
                        child.key.isSynced = false

                        domain.deleteChildTask(child.key)
                    }

                    DomainState.Recover -> {
                        db.taskHierarchyDao().setDeleted(this.uuid, child.key.uuid)
                        db.taskHierarchyDao().setSynced(this.uuid, child.key.uuid, false)
                        child.key.isDeleted = true
                        child.key.isSynced = false

                        domain.setReadChildTask(child.key)
                    }

                    DomainState.Read -> {}
                }
            }
        }
        domain.run {
            parentTasks.forEach { parent ->

                when (parent.value) {
                    DomainState.Insert -> {
                        db.taskHierarchyDao().insert(
                            TaskHierarchy(
                                parentId = parent.key.uuid,
                                childId = this.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )

                        domain.setReadParentTask(parent.key)
                    }

                    DomainState.Delete -> {
                        db.taskHierarchyDao().setDeleted(parent.key.uuid, this.uuid)
                        db.taskHierarchyDao().setSynced(parent.key.uuid, this.uuid, false)
                        parent.key.isDeleted = true
                        parent.key.isSynced = false

                        domain.deleteParentTask(parent.key)
                    }

                    DomainState.Recover -> {
                        db.taskHierarchyDao().setDeleted(parent.key.uuid, this.uuid, false)
                        db.taskHierarchyDao().setSynced(parent.key.uuid, this.uuid, false)
                        parent.key.isDeleted = true
                        parent.key.isSynced = false

                        domain.setReadParentTask(parent.key)
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

                        domain.setReadTag(tag.key)
                    }

                    DomainState.Delete -> {
                        db.taskTagDao().setDeleted(this.uuid, tag.key.uuid)
                        db.taskTagDao().setSynced(this.uuid, tag.key.uuid, false)
                        tag.key.isDeleted = true
                        tag.key.isSynced = false

                        domain.addTag(tag.key)
                    }

                    DomainState.Recover -> {
                        db.taskTagDao().setDeleted(this.uuid, tag.key.uuid, false)
                        db.taskTagDao().setSynced(this.uuid, tag.key.uuid, false)
                        tag.key.isDeleted = true
                        tag.key.isSynced = false

                        domain.setReadTag(tag.key)
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

                        domain.setReadResource(resource.key)
                    }

                    DomainState.Delete -> {
                        db.taskResourceDao().setDeleted(resource.key.uuid, this.uuid)
                        db.taskResourceDao().setSynced(resource.key.uuid, this.uuid, false)
                        resource.key.isDeleted = true
                        resource.key.isSynced = false

                        domain.deleteResource(resource.key)
                    }

                    DomainState.Recover -> {
                        db.taskResourceDao().setDeleted(resource.key.uuid, this.uuid, false)
                        db.taskResourceDao().setSynced(resource.key.uuid, this.uuid, false)
                        resource.key.isDeleted = true
                        resource.key.isSynced = false

                        domain.setReadResource(resource.key)
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
                                taskId = domain.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )

                        domain.setReadNote(note.key)
                    }

                    DomainState.Delete -> {
                        db.noteTaskDao().setDeleted(note.key.uuid, this.uuid)
                        db.noteTaskDao().setSynced(note.key.uuid, this.uuid, false)
                        note.key.isDeleted = true
                        note.key.isSynced = false

                        domain.deleteNote(note.key)
                    }

                    DomainState.Recover -> {
                        db.noteTaskDao().setDeleted(note.key.uuid, this.uuid, false)
                        db.noteTaskDao().setSynced(note.key.uuid, this.uuid, false)
                        note.key.isDeleted = true
                        note.key.isSynced = false

                        domain.setReadNote(note.key)
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }

    suspend fun updateExecutors(domain: Task) {
        domain.run {
            executors.forEach { user ->
                when (user.value) {
                    DomainState.Insert -> {
                        db.taskAssignmentDao().insert(
                            TaskAssignment(
                                taskId = domain.uuid,
                                userId = user.key.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )

                        domain.setReadExecutor(user.key)
                    }

                    DomainState.Delete -> {
                        db.taskAssignmentDao().setDelete(user.key.uuid, this.uuid, true)
                        db.taskAssignmentDao().setSynced(user.key.uuid, this.uuid, false)
                        user.key.isDeleted = true
                        user.key.isSynced = false

                        domain.deleteExecutor(user.key)
                    }

                    DomainState.Recover -> {
                        db.taskAssignmentDao().setDelete(user.key.uuid, this.uuid, false)
                        db.taskAssignmentDao().setSynced(user.key.uuid, this.uuid, false)
                        user.key.isDeleted = false
                        user.key.isSynced = false

                        domain.setReadExecutor(user.key)
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }
}