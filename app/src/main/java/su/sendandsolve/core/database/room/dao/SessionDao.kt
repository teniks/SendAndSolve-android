package su.sendandsolve.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.core.database.room.entity.Session
import java.util.UUID

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = false): Flow<List<Session>>

    @Query("SELECT * FROM sessions WHERE uuid = :sessionId AND is_deleted = :isDeleted")
    suspend fun getById(sessionId:UUID, isDeleted: Boolean = false): Session?

    @Query("SELECT * FROM sessions WHERE session_token = :sessionToken AND is_deleted = :isDeleted")
    suspend fun getByToken(sessionToken:String, isDeleted: Boolean = false): Session?

    @Query("SELECT * FROM sessions WHERE user_id = :userId AND is_deleted = :isDeleted")
    fun getByUser(userId:UUID, isDeleted: Boolean = false): Flow<List<Session>>

    @Insert
    suspend fun insert(session: Session)

    @Update
    suspend fun update(session: Session)

    @Query("UPDATE sessions SET is_deleted = :isDeleted WHERE uuid = :sessionId")
    suspend fun setDeleted(sessionId: UUID, isDeleted: Boolean = true)

    @Query("UPDATE sessions SET is_synced = :isSynced WHERE uuid = :sessionId")
    suspend fun setSynced(sessionId: UUID, isSynced: Boolean = true)

}