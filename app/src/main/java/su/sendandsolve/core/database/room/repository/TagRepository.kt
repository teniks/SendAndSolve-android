package su.sendandsolve.core.database.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.database.room.entity.NoteTag
import su.sendandsolve.core.database.room.entity.TaskTag
import su.sendandsolve.core.database.room.mapper.TagMapper
import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.Tag
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class TagRepository @Inject constructor(
    private val db: RoomAppDatabase
) : Repository<Tag> {
    override suspend fun getById(id: UUID): Tag? {
        return db.tagDao().getById(id)?.let { TagMapper.toDomain(it) }
    }

    override fun getAll(isDeleted: Boolean): Flow<List<Tag>> {
        return db.tagDao().getDeleted(isDeleted).map { it.map(TagMapper::toDomain)}
    }

    fun getByTask(taskId: UUID, isDeleted: Boolean = false): Flow<List<Tag>> {
        return db.tagDao().getByTask(taskId, isDeleted).map { it.map(TagMapper::toDomain) }
    }

    override suspend fun delete(domain: Tag) {
        db.tagDao().setDeleted(domain.uuid)
    }

    override suspend fun update(domain: Tag) {
        db.tagDao().update(TagMapper.toEntity(domain))
    }

    override suspend fun insert(domain: Tag) {
        db.tagDao().insert(TagMapper.toEntity(domain))
    }

    suspend fun updateTasks(domain: Tag) {
        domain.run {
            tasks.forEach { task ->
                when(task.value) {
                    DomainState.Insert -> {
                        db.taskTagDao().insert(
                            TaskTag(
                                taskId = task.key.uuid,
                                tagId = this.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now())
                        )
                        tasks[task.key] = DomainState.Read
                    }

                    DomainState.Delete -> {
                        db.taskTagDao().setDeleted(task.key.uuid, this.uuid)
                        db.taskTagDao().setSynced(task.key.uuid, this.uuid, false)
                        task.key.isDeleted = true
                        task.key.isSynced = false
                        tasks[task.key] = DomainState.Read
                    }

                    DomainState.Recover -> {
                        db.taskTagDao().setDeleted(task.key.uuid, this.uuid, false)
                        db.taskTagDao().setSynced(task.key.uuid, this.uuid, false)
                        task.key.isDeleted = false
                        task.key.isSynced = false
                        tasks[task.key] = DomainState.Read
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }

    suspend fun updateNotes(domain: Tag) {
        domain.run {
            notes.forEach { note ->
                when (note.value) {
                    DomainState.Insert -> {
                        db.noteTagDao().insert(
                            NoteTag(
                                noteId = note.key.uuid,
                                tagId = this.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )
                        notes[note.key] = DomainState.Read
                    }

                    DomainState.Delete -> {
                        db.noteTagDao().setDeleted(note.key.uuid, this.uuid)
                        db.taskTagDao().setSynced(note.key.uuid, this.uuid, false)
                        note.key.isDeleted = true
                        note.key.isSynced = false
                        notes[note.key] = DomainState.Read
                    }

                    DomainState.Recover -> {
                        db.noteTagDao().setDeleted(note.key.uuid, this.uuid, false)
                        db.taskTagDao().setSynced(note.key.uuid, this.uuid, false)
                        note.key.isDeleted = false
                        note.key.isSynced = false
                        notes[note.key] = DomainState.Read
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }
}