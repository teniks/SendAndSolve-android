package su.sendandsolve.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.core.database.room.entity.ChangeLog
import java.util.UUID

@Dao
interface ChangeLogDao {
    @Query("SELECT * FROM changelogs WHERE is_deleted = :isDeleted")
    fun getAll(isDeleted: Boolean = false): Flow<List<ChangeLog>>

    @Query("SELECT * FROM changelogs WHERE uuid = :logId AND is_deleted = :isDeleted")
    suspend fun getById(logId: UUID, isDeleted: Boolean = false): ChangeLog?

    @Query("SELECT * FROM changelogs WHERE operation_group_id = :operationId AND is_deleted = :isDeleted")
    fun getByOperationId(operationId: UUID, isDeleted: Boolean = false): Flow<List<ChangeLog>>

    @Query("SELECT * FROM changelogs WHERE user_id = :userId AND is_deleted = :isDeleted")
    fun getByUser(userId: UUID, isDeleted: Boolean = false): Flow<List<ChangeLog>>

    @Query("SELECT * FROM changelogs WHERE device_id = :deviceId AND is_deleted = :isDeleted")
    fun getByDevice(deviceId: UUID, isDeleted: Boolean = false): Flow<List<ChangeLog>>

    @Query("SELECT * FROM changelogs WHERE table_name = :tableName AND is_deleted = :isDeleted")
    fun getByTableName(tableName: String, isDeleted: Boolean = false): Flow<List<ChangeLog>>

    @Query("SELECT * FROM changelogs WHERE timestamp = :timestamp AND is_deleted = :isDeleted")
    fun getByTimestamp(timestamp: String, isDeleted: Boolean = false): Flow<List<ChangeLog>>

    @Query("SELECT * FROM changelogs WHERE is_synced = :isSynced")
    fun getByIsSynced(isSynced: Boolean): Flow<List<ChangeLog>>

    @Query("SELECT * FROM changelogs WHERE is_deleted = :isDeleted")
    fun getByIsDeleted(isDeleted: Boolean = false): Flow<List<ChangeLog>>

    @Insert
    suspend fun insert(log: ChangeLog)

    @Update
    suspend fun update(log: ChangeLog)

    @Query("UPDATE changelogs SET is_synced = :isSynced WHERE uuid = :logId")
    suspend fun setSynced(logId: UUID, isSynced: Boolean = true)

    @Query("UPDATE changelogs SET is_deleted = :isDeleted WHERE uuid = :logId")
    suspend fun setDelete(logId: UUID, isDeleted: Boolean = true)
}