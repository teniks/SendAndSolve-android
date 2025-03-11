package su.sendandsolve.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.data.db.room.entity.Note
import java.util.UUID

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = false): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE uuid = :noteId AND is_deleted = :isDeleted")
    suspend fun getById(noteId: UUID, isDeleted: Boolean = false): Note?

    @Query("SELECT * FROM notes WHERE is_synced = :isSynced")
    fun getSynced(isSynced: Boolean = false): Flow<List<Note>>

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Query("UPDATE notes SET is_synced = :isSynced WHERE uuid = :noteId")
    suspend fun setSynced(noteId: UUID, isSynced: Boolean)

    @Query("UPDATE notes SET is_deleted = :isDeleted WHERE uuid = :noteId")
    suspend fun setDelete(noteId: UUID, isDeleted: Boolean)
}