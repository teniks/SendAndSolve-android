package su.sendandsolve.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.data.db.room.entity.Device
import java.util.UUID

@Dao
interface DeviceDao {
    @Query("SELECT * FROM devices WHERE uuid = :deviceId AND is_deleted = :isDeleted")
    suspend fun getById(deviceId: UUID, isDeleted: Boolean = false): Device?

    @Query("SELECT * FROM devices WHERE is_synced = :isSynced")
    fun getByIsSynced(isSynced: Boolean = false): Flow<List<Device>>

    @Query("SELECT * FROM devices WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = false): Flow<List<Device>>

    @Insert
    suspend fun insert(device: Device)

    @Update
    suspend fun update(device: Device)

    @Query("UPDATE devices SET is_synced = :isSynced WHERE uuid = :deviceId")
    suspend fun setSynced(deviceId: UUID, isSynced: Boolean)

    @Query("UPDATE devices SET is_deleted = :isDeleted WHERE uuid = :deviceId")
    suspend fun setDelete(deviceId: UUID, isDeleted: Boolean = true)
}