package su.sendandsolve.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.core.database.room.entity.TaskGroup
import java.util.UUID

@Dao
interface TaskGroupDao {
    @Query("SELECT * FROM task_groups WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = true): Flow<List<TaskGroup>>

    @Query("SELECT * FROM task_groups WHERE is_synced = :isSynced")
    fun getSynced(isSynced: Boolean = false): Flow<List<TaskGroup>>

    @Query("SELECT * FROM task_groups WHERE task_id = :taskId AND is_deleted = :isDeleted")
    fun getByTask(taskId: UUID, isDeleted: Boolean = false): Flow<List<TaskGroup>>

    @Query("SELECT * FROM task_groups WHERE group_id = :groupId AND is_deleted = :isDeleted")
    fun getByGroup(groupId: UUID, isDeleted: Boolean = false): Flow<List<TaskGroup>>

    @Insert
    suspend fun insert(taskGroup: TaskGroup)

    @Update
    suspend fun update(taskGroup: TaskGroup)

    @Query("UPDATE task_groups SET is_deleted = :isDeleted WHERE group_id = :groupId AND task_id = :taskId")
    suspend fun setDeleted(groupId: UUID, taskId: UUID, isDeleted: Boolean = true)

    @Query("UPDATE task_groups SET is_synced = :isSynced WHERE group_id = :groupId AND task_id = :taskId")
    suspend fun setSynced(groupId: UUID, taskId: UUID, isSynced: Boolean = true)

}