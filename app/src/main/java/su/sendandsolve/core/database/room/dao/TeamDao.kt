package su.sendandsolve.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.core.database.room.entity.Team
import java.util.UUID

@Dao
interface TeamDao {
    @Query("SELECT * FROM teams WHERE is_deleted = :isDeleted")
    fun getAll(isDeleted: Boolean = false): Flow<List<Team>>

    @Query("SELECT * FROM teams WHERE uuid = :uuid AND is_deleted = :isDeleted")
    suspend fun getById(uuid: UUID, isDeleted: Boolean = false): Team?

    @Query("SELECT * FROM teams WHERE name = :name AND is_deleted = :isDeleted")
    suspend fun getByName(name: String, isDeleted: Boolean = false): Team?

    @Query("SELECT * FROM teams WHERE is_synced = :isSynced")
    fun getByIsSynced(isSynced: Boolean = false): Flow<List<Team>>

    @Query("SELECT * FROM teams WHERE creator_id = :idCreator AND is_deleted = :isDeleted")
    fun getByCreator(idCreator: UUID, isDeleted: Boolean = false): Flow<List<Team>>

    @Insert
    suspend fun insert(team: Team)

    @Update
    suspend fun update(team: Team)

    @Query("UPDATE teams SET is_synced = :isSynced WHERE uuid = :uuid")
    suspend fun setSynced(uuid: UUID, isSynced: Boolean = true)

    @Query("UPDATE teams SET is_deleted = :isDeleted WHERE uuid = :uuid")
    suspend fun setDelete(uuid: UUID, isDeleted: Boolean = true)
}