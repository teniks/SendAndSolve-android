package su.sendandsolve.data.db.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.data.db.room.RoomAppDatabase
import su.sendandsolve.data.db.room.dao.NoteTagDao
import su.sendandsolve.data.db.room.dao.TagDao
import su.sendandsolve.data.db.room.dao.TaskTagDao
import su.sendandsolve.data.db.room.entity.NoteTag
import su.sendandsolve.data.db.room.entity.TaskTag
import su.sendandsolve.data.db.room.mapper.TagMapper
import su.sendandsolve.data.domain.DomainState
import su.sendandsolve.data.domain.Repository
import su.sendandsolve.data.domain.model.Tag
import java.time.Instant
import java.util.UUID

class TagRepository(
    private val db: RoomAppDatabase
) : Repository<Tag> {
    override suspend fun getById(id: UUID): Tag? {
        return db.tagDao().getById(id)?.let { TagMapper.toDomain(it) }
    }

    override fun getAll(isDeleted: Boolean): Flow<List<Tag>> {
        return db.tagDao().getDeleted(isDeleted).map { it.map(TagMapper::toDomain)}
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
                                lastModified = Instant.now()))
                        tasks[task.key] = DomainState.Read
                    }

                    DomainState.Delete -> {
                        db.taskTagDao().setDeleted(task.key.uuid, this.uuid)
                        task.key.isDeleted = true
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
                        note.key.isDeleted = true
                        note.key.isSynced = false
                        notes[note.key] = DomainState.Read
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }
}