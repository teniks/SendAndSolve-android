package su.sendandsolve.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.data.db.room.entity.Resource
import java.util.UUID

@Dao
interface ResourceDao {
    @Query("SELECT * FROM resources WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = false): Flow<List<Resource>>

    @Query("SELECT * FROM resources WHERE uuid = :resourceId AND is_deleted = :isDeleted")
    suspend fun getById(resourceId: UUID, isDeleted: Boolean = false): Resource?

    @Query("SELECT * FROM resources WHERE is_synced = :isSynced")
    fun getSynced(isSynced: Boolean = false): Flow<List<Resource>>

    @Query("SELECT * FROM resources WHERE creator_id = :creatorId AND is_deleted = :isDeleted")
    fun getByCreator(creatorId: UUID, isDeleted: Boolean = false): Flow<List<Resource>>

    @Insert
    suspend fun insert(resource: Resource)

    @Update
    suspend fun update(resource: Resource)

    @Query("UPDATE resources SET is_deleted = :isDeleted WHERE uuid = :resourceId")
    suspend fun setDelete(resourceId: UUID, isDeleted: Boolean = true)

    @Query("UPDATE resources SET is_synced = :isSynced WHERE uuid = :resourceId")
    suspend fun setSynced(resourceId: UUID, isSynced: Boolean = true)
}