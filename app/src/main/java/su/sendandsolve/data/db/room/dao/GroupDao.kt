package su.sendandsolve.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.data.db.room.entity.Group
import java.util.UUID

@Dao
interface GroupDao {
    @Query("SELECT * FROM groups WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = true): Flow<List<Group>>

    @Query("SELECT * FROM groups WHERE is_synced = :isSynced")
    fun getSynced(isSynced: Boolean = false): Flow<List<Group>>

    @Query("SELECT * FROM groups WHERE uuid = :groupId AND is_deleted = :isDeleted")
    suspend fun getById(groupId: UUID, isDeleted: Boolean = false): Group?

    @Insert
    suspend fun insert(group: Group)

    @Update
    suspend fun update(group: Group)

    @Query("UPDATE groups SET is_deleted = :isDeleted WHERE uuid = :groupId")
    suspend fun setDeleted(groupId: UUID, isDeleted: Boolean = true)

    @Query("UPDATE groups SET is_synced = :isSynced WHERE uuid = :groupId")
    suspend fun setSynced(groupId: UUID, isSynced: Boolean = true)
}