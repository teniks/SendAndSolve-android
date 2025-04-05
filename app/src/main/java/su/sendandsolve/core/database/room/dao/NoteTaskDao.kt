package su.sendandsolve.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.core.database.room.entity.Note
import su.sendandsolve.core.database.room.entity.NoteTask
import su.sendandsolve.core.database.room.entity.Task
import java.util.UUID

@Dao
interface NoteTaskDao {
    @Query("SELECT * FROM tasks WHERE uuid IN (SELECT task_id FROM note_tasks WHERE note_id = :noteId AND is_deleted = :isDeleted) AND is_deleted = :isDeleted")
    fun getTasks(noteId: UUID, isDeleted: Boolean = false): Flow<List<Task>>

    @Query("SELECT * FROM notes WHERE uuid IN (SELECT note_id FROM note_tasks WHERE task_id = :taskId AND is_deleted = :isDeleted) AND is_deleted = :isDeleted")
    fun getNotes(taskId: UUID, isDeleted: Boolean = false): Flow<List<Note>>

    @Query("SELECT * FROM note_tasks WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = false): Flow<List<NoteTask>>

    @Query("SELECT * FROM note_tasks WHERE is_synced = :isSynced")
    fun getSynced(isSynced: Boolean = false): Flow<List<NoteTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskTag: NoteTask)

    @Update
    suspend fun update(taskTag: NoteTask)

    @Query("UPDATE note_tasks SET is_deleted = :isDeleted WHERE note_id = :noteId AND task_id = :taskId")
    suspend fun setDeleted(noteId: UUID, taskId: UUID, isDeleted: Boolean = true)

    @Query("UPDATE note_tasks SET is_synced = :isSynced WHERE note_id = :noteId AND task_id = :taskId")
    suspend fun setSynced(noteId: UUID, taskId: UUID, isSynced: Boolean = true)
}