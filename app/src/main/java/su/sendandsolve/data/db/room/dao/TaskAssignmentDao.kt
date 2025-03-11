package su.sendandsolve.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.data.db.room.entity.Task
import su.sendandsolve.data.db.room.entity.TaskAssignment
import su.sendandsolve.data.db.room.entity.User
import java.util.UUID

@Dao
interface TaskAssignmentDao {
    @Transaction
    @Query("SELECT * FROM tasks WHERE uuid IN (SELECT task_id FROM task_assignments WHERE user_id = :userId AND is_deleted = :isDeleted) AND is_deleted = :isDeleted")
    fun getTasksByUser(userId: UUID, isDeleted: Boolean = false): Flow<List<Task>>

    @Transaction
    @Query("SELECT * FROM users WHERE uuid IN (SELECT user_id FROM task_assignments WHERE task_id = :taskId AND is_deleted = :isDeleted) AND is_deleted = :isDeleted")
    fun getUsersByTask(taskId: UUID, isDeleted: Boolean = false): Flow<List<User>>

    @Query("SELECT * FROM task_assignments WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = true): Flow<List<TaskAssignment>>

    @Query("SELECT * FROM task_assignments WHERE is_synced = :isSynced")
    fun getSynced(isSynced: Boolean = true): Flow<List<TaskAssignment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskAssignment: TaskAssignment)

    @Query("UPDATE task_assignments SET is_synced = :isSynced WHERE task_id = :taskId AND user_id = :userId")
    suspend fun setSynced(taskId: UUID, userId: UUID, isSynced: Boolean = true)

    @Query("UPDATE task_assignments SET is_deleted = :isDeleted WHERE task_id = :taskId AND user_id = :userId")
    suspend fun setDelete(taskId: UUID, userId: UUID, isDeleted: Boolean = true)
}