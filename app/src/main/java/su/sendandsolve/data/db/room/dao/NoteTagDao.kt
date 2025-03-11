package su.sendandsolve.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.data.db.room.entity.Note
import su.sendandsolve.data.db.room.entity.NoteTag
import su.sendandsolve.data.db.room.entity.Tag
import java.util.UUID

@Dao
interface NoteTagDao {
    @Transaction
    @Query("SELECT * FROM tags WHERE uuid IN (SELECT tag_id from note_tags WHERE note_id = :noteId AND is_deleted = :isDeleted) AND is_deleted = :isDeleted")
    fun getTagsByNote(noteId: UUID, isDeleted: Boolean = false): Flow<List<Tag>>

    @Transaction
    @Query("SELECT * FROM notes WHERE uuid IN (SELECT note_id from note_tags WHERE tag_id = :tagId AND is_deleted = :isDeleted) AND is_deleted = :isDeleted")
    fun getNotesByTag(tagId: UUID, isDeleted: Boolean = false): Flow<List<Note>>

    @Query("SELECT * FROM note_tags WHERE is_synced = :isSynced")
    fun getSynced(isSynced: Boolean = false): Flow<List<NoteTag>>

    @Query("SELECT * FROM note_tags WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = true): Flow<List<NoteTag>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteTag: NoteTag)

    @Query("UPDATE note_tags SET is_deleted = :isDeleted WHERE note_id = :noteId AND tag_id = :tagId")
    suspend fun setDeleted(noteId: UUID, tagId: UUID, isDeleted: Boolean = true)

    @Query("UPDATE note_tags SET is_synced = :isSynced WHERE note_id = :noteId AND tag_id = :tagId")
    suspend fun setSynced(noteId: UUID, tagId: UUID, isSynced: Boolean = true)
}