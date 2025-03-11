package su.sendandsolve.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import su.sendandsolve.data.db.room.entity.Resource
import su.sendandsolve.data.db.room.entity.Task
import su.sendandsolve.data.db.room.entity.TaskResource
import java.util.UUID

@Dao
interface TaskResourceDao {
    @Transaction
    @Query("SELECT * FROM resources WHERE uuid IN (SELECT resource_id FROM task_resources WHERE task_id = :idTask) AND is_deleted = :isDeleted")
    fun getResourcesByTask(idTask: UUID, isDeleted: Boolean = false): List<Resource>

    @Transaction
    @Query("SELECT * FROM tasks WHERE uuid IN (SELECT task_id FROM task_resources WHERE resource_id = :idResource) AND is_deleted = :isDeleted")
    fun getTasksByResource(idResource: UUID, isDeleted: Boolean = false): List<Task>

    @Query("SELECT * FROM task_resources WHERE is_synced = :isSynced")
    fun getSynced(isSynced: Boolean = true): List<TaskResource>

    @Query("SELECT * FROM task_resources WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = true): List<TaskResource>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskResource: TaskResource)

    @Query("UPDATE task_resources SET is_deleted = :isDeleted WHERE task_id = :idTask AND resource_id = :idResource")
    suspend fun setDeleted(idTask: UUID, idResource: UUID, isDeleted: Boolean = true)

    @Query("UPDATE task_resources SET is_synced = :isSynced WHERE task_id = :idTask AND resource_id = :idResource")
    suspend fun setSynced(idTask: UUID, idResource: UUID, isSynced: Boolean = true)
}