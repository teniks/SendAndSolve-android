package su.sendandsolve.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.core.database.room.entity.Task
import java.time.Instant
import java.util.UUID

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE is_deleted = :isDeleted ORDER BY date_creation DESC LIMIT :limit OFFSET :offset")
    suspend fun getTasks(limit: Int, offset: Int, isDeleted: Boolean = false): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE uuid = :taskId")
    suspend fun getById(taskId: UUID): Task?

    @Query("SELECT * FROM tasks WHERE creator_id = :userId AND is_deleted = :isDeleted")
    fun getByUser(userId: UUID, isDeleted: Boolean = false): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE is_deleted = :isDeleted")
    fun getByIsDeleted(isDeleted: Boolean = false): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE end_date <= :date AND is_deleted = :isDeleted")
    fun getByEndDate(date: Instant, isDeleted: Boolean = false): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE start_date >= :date AND is_deleted = :isDeleted")
    fun getByStartDate(date: Instant, isDeleted: Boolean = false): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE priority = :priority AND is_deleted = :isDeleted")
    fun getByPriority(priority: String, isDeleted: Boolean = false): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE status = :status AND is_deleted = :isDeleted")
    fun getByStatus(status: String, isDeleted: Boolean = false): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE team_id = :teamId AND is_deleted = :isDeleted")
    fun getByTeam(teamId: UUID, isDeleted: Boolean = false): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE is_synced = :isSynced")
    fun getByIsSynced(isSynced: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE last_modified = :lastModified AND is_deleted = :isDeleted")
    fun getByLastModified(lastModified: Instant, isDeleted: Boolean = false): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = false): Flow<List<Task>>

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("UPDATE tasks SET is_synced = :isSynced WHERE uuid = :taskId")
    suspend fun setSynced(taskId: UUID, isSynced: Boolean = true)

    @Query("UPDATE tasks SET is_deleted = :isDeleted WHERE uuid = :taskId")
    suspend fun setDeleted(taskId: UUID, isDeleted: Boolean = true)
}