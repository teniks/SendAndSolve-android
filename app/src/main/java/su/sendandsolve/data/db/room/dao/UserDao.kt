package su.sendandsolve.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.data.db.room.entity.User
import java.util.UUID

@Dao
interface UserDao {
    @Query("SELECT * FROM Users WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = false): Flow<List<User>>

    @Query("SELECT * FROM Users WHERE uuid = :userId AND is_deleted = :isDeleted")
    suspend fun getById(userId: UUID, isDeleted: Boolean = false): User?

    @Query("SELECT * FROM Users WHERE is_deleted = :isDeleted")
    fun getByIsDeleted(isDeleted: Boolean = false): Flow<List<User>>

    @Query("SELECT * FROM Users WHERE is_synced = :isSynced")
    fun getByIsSynced(isSynced: Boolean = false): Flow<List<User>>

    // при конфликте прерываем транзакцию
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("UPDATE Users SET is_synced = :isSynced WHERE uuid = :userId")
    suspend fun setSynced(userId: UUID, isSynced: Boolean)

    @Query("UPDATE Users SET is_deleted = :isDeleted WHERE uuid = :userId")
    suspend fun setDeleted(userId: UUID, isDeleted: Boolean = true)
}