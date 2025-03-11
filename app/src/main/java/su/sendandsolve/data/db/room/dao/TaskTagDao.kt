package su.sendandsolve.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import su.sendandsolve.data.db.room.entity.Tag
import su.sendandsolve.data.db.room.entity.Task
import su.sendandsolve.data.db.room.entity.TaskTag
import java.util.UUID

@Dao
interface TaskTagDao {
    @Transaction
    @Query("SELECT * FROM tags WHERE uuid IN (SELECT uuid FROM task_tags WHERE uuid = :taskId AND is_deleted = :isDeleted) AND is_deleted = :isDeleted")
    fun getTags(taskId: UUID, isDeleted: Boolean = false): List<Tag>

    @Transaction
    @Query("SELECT * FROM tasks WHERE uuid IN (SELECT uuid FROM task_tags WHERE uuid = :taskId AND is_deleted = :isDeleted) AND is_deleted = :isDeleted")
    fun getTasks(taskId: UUID, isDeleted: Boolean = true): List<Task>

    @Query("SELECT * FROM task_tags WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = false): List<TaskTag>

    @Query("SELECT * FROM task_tags WHERE is_synced = :isSynced")
    fun getSynced(isSynced: Boolean = false): List<TaskTag>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskTag: TaskTag)

    @Query("UPDATE task_tags SET is_synced = :isSynced WHERE tag_id = :tagId AND task_id = :taskId")
    suspend fun setSynced(taskId: UUID, tagId: UUID, isSynced: Boolean = true)

    @Query("UPDATE task_tags SET is_deleted = :isDeleted WHERE tag_id = :tagId AND task_id = :taskId")
    suspend fun setDeleted(taskId: UUID, tagId: UUID, isDeleted: Boolean = true)
}