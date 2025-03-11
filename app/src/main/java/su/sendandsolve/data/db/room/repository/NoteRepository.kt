package su.sendandsolve.data.db.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.data.db.room.RoomAppDatabase
import su.sendandsolve.data.db.room.dao.NoteDao
import su.sendandsolve.data.db.room.dao.NoteTagDao
import su.sendandsolve.data.db.room.dao.NoteTaskDao
import su.sendandsolve.data.db.room.entity.NoteTag
import su.sendandsolve.data.db.room.entity.NoteTask
import su.sendandsolve.data.db.room.mapper.NoteMapper
import su.sendandsolve.data.domain.DomainState
import su.sendandsolve.data.domain.model.Note
import su.sendandsolve.data.domain.Repository
import java.time.Instant
import java.util.UUID

class NoteRepository(
    private val db: RoomAppDatabase
) : Repository<Note> {
    override suspend fun getById(id: UUID): Note? {
        return db.noteDao().getById(id)?.let { NoteMapper.toDomain(it) }
    }

    override fun getAll(isDeleted: Boolean): Flow<List<Note>> {
        return db.noteDao().getDeleted(isDeleted).map { list -> list.map(NoteMapper::toDomain) }
    }

    override suspend fun delete(domain: Note) {
        db.noteDao().setDelete(domain.uuid, true)
    }

    override suspend fun update(domain: Note) {
        db.noteDao().update(NoteMapper.toEntity(domain))
    }

    override suspend fun insert(domain: Note) {
        db.noteDao().insert(NoteMapper.toEntity(domain))
    }

    suspend fun updateTasks(domain: Note) {
        domain.run {
            tasks.forEach { task ->
                when(task.value){
                    DomainState.Insert -> {
                        db.noteTaskDao().insert(
                            NoteTask(
                                noteId = this.uuid,
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
                        db.noteTaskDao().setDeleted(this.uuid, task.key.uuid)
                        task.key.isDeleted = true
                        task.key.isSynced = false
                        tasks[task.key] = DomainState.Read
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }

    suspend fun updateTags(domain: Note) {
        domain.run {
            tags.forEach { tag ->
                when (tag.value) {
                    DomainState.Insert -> {
                        db.noteTagDao().insert(
                            NoteTag(
                                noteId = this.uuid,
                                tagId = tag.key.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )
                        tags[tag.key] = DomainState.Read
                    }

                    DomainState.Delete -> {
                        db.noteTagDao().setDeleted(this.uuid, tag.key.uuid)
                        tag.key.isDeleted = true
                        tag.key.isSynced = false
                        tags[tag.key] = DomainState.Read
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }


}