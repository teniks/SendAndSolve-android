package su.sendandsolve.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.core.database.room.entity.Tag
import java.util.UUID

@Dao
interface TagDao {
    @Query("SELECT * FROM tags WHERE is_deleted = :isDeleted LIMIT :limit OFFSET :offset")
    fun getTags(limit: Int, offset: Int, isDeleted: Boolean = false): Flow<List<Tag>>

    @Query("SELECT * FROM tags WHERE uuid = :tagId AND is_deleted = :isDeleted")
    suspend fun getById(tagId: UUID, isDeleted: Boolean = false): Tag?

    @Query("SELECT * FROM tags WHERE uuid IN (SELECT tag_id FROM task_tags WHERE task_id = :taskId AND is_deleted = :isDeleted)")
    fun getByTask(taskId: UUID, isDeleted: Boolean = false): Flow<List<Tag>>

    @Query("SELECT * FROM tags WHERE is_synced = :isSynced")
    fun getSynced(isSynced: Boolean = false): Flow<List<Tag>>

    @Query("SELECT * FROM tags WHERE tag = :tag AND is_deleted = :isDeleted")
    suspend fun getByName(tag: String, isDeleted: Boolean = false): Tag?

    @Query("SELECT * FROM tags WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = true): Flow<List<Tag>>

    @Insert
    suspend fun insert(tag: Tag)

    @Update
    suspend fun update(tag: Tag)

    @Query("UPDATE tags SET is_synced = :isSynced WHERE uuid = :tagId")
    suspend fun setSynced(tagId: UUID, isSynced: Boolean = true)

    @Query("UPDATE tags SET is_deleted = :isDeleted WHERE uuid = :tagId")
    suspend fun setDeleted(tagId: UUID, isDeleted: Boolean = true)
}