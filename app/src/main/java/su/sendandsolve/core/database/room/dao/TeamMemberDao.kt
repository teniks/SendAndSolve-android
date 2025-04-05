package su.sendandsolve.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import su.sendandsolve.core.database.room.entity.Team
import su.sendandsolve.core.database.room.entity.TeamMember
import su.sendandsolve.core.database.room.entity.User
import java.util.UUID

@Dao
interface TeamMemberDao {
    @Transaction
    @Query("SELECT * FROM users WHERE uuid IN (SELECT user_id FROM team_members WHERE team_id = :teamId AND is_deleted = :isDeleted) AND is_deleted = :isDeleted")
    fun getMembersByTeam(teamId: UUID, isDeleted: Boolean = false): Flow<List<User>>

    @Transaction
    @Query("SELECT * FROM teams WHERE uuid IN (SELECT user_id FROM team_members WHERE user_id = :memberId AND is_deleted = :isDeleted) AND is_deleted = :isDeleted")
    fun getTeamsByMember(memberId: UUID, isDeleted: Boolean = false): Flow<List<Team>>

    @Transaction
    @Query("SELECT * FROM users WHERE uuid IN (SELECT user_id FROM team_members WHERE team_role = :teamRole AND is_deleted = :isDeleted) AND is_deleted = :isDeleted")
    fun getMembersByRole(teamRole: String, isDeleted: Boolean = false): Flow<List<User>>

    @Query("SELECT * FROM team_members WHERE is_deleted = :isDeleted")
    fun getDeleted(isDeleted: Boolean = false): Flow<List<TeamMember>>

    @Query("SELECT * FROM team_members WHERE is_synced = :isSynced")
    fun getSynced(isSynced: Boolean = false): Flow<List<TeamMember>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(teamMember: TeamMember)

    @Query("UPDATE team_members SET is_synced = :isSynced WHERE user_id = :memberId AND team_id = :teamId")
    suspend fun setSynced(memberId: UUID, teamId: UUID, isSynced: Boolean = true)

    @Query("UPDATE team_members SET is_deleted = :isDeleted WHERE user_id = :memberId AND team_id = :teamId")
    suspend fun setDeleted(memberId: UUID, teamId: UUID, isDeleted: Boolean = true)
}