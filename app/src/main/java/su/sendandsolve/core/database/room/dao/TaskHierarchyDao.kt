package su.sendandsolve.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import su.sendandsolve.core.database.room.entity.Task
import su.sendandsolve.core.database.room.entity.TaskHierarchy
import java.util.UUID

@Dao
interface TaskHierarchyDao {
    @Transaction
    @Query("SELECT * FROM tasks WHERE uuid IN (SELECT uuid FROM task_hierarchy WHERE child_id = :childId AND is_deleted = :isDeleted) AND is_deleted = :isDeleted")
    fun getParents(childId: UUID, isDeleted: Boolean = false): List<Task>

    @Transaction
    @Query("SELECT * FROM tasks WHERE uuid IN (SELECT uuid FROM task_hierarchy WHERE parent_id = :parentId AND is_deleted = :isDeleted) AND is_deleted = :isDeleted")
    fun getChildren(parentId: UUID, isDeleted: Boolean = false): List<Task>

    @Query("SELECT * FROM task_hierarchy WHERE is_synced = :isSynced")
    fun getSynced(isSynced: Boolean = true): List<TaskHierarchy>

    @Query("SELECT * FROM task_hierarchy WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = true): List<TaskHierarchy>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskHierarchy: TaskHierarchy)

    @Query("UPDATE task_hierarchy SET is_deleted = :isDeleted WHERE parent_id = :parentId AND child_id = :childId")
    suspend fun setDeleted(parentId: UUID, childId: UUID, isDeleted: Boolean = true)

    @Query("UPDATE task_hierarchy SET is_synced = :isSynced WHERE parent_id = :parentId AND child_id = :childId")
    suspend fun setSynced(parentId: UUID, childId: UUID, isSynced: Boolean = true)
}