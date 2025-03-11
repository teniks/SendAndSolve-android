package su.sendandsolve.data.db.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.data.db.room.RoomAppDatabase
import su.sendandsolve.data.db.room.dao.TeamDao
import su.sendandsolve.data.db.room.dao.TeamMemberDao
import su.sendandsolve.data.db.room.entity.TeamMember
import su.sendandsolve.data.db.room.mapper.TeamMapper
import su.sendandsolve.data.domain.DomainState
import su.sendandsolve.data.domain.Repository
import su.sendandsolve.data.domain.model.Team
import java.time.Instant
import java.util.UUID

class TeamRepository(
    private val db: RoomAppDatabase
) : Repository<Team> {
    override suspend fun getById(id: UUID): Team? {
        return db.teamDao().getById(id)?.let { TeamMapper.toDomain(it) }
    }

    override fun getAll(isDeleted: Boolean): Flow<List<Team>> {
        return db.teamDao().getAll(isDeleted).map { it.map(TeamMapper::toDomain) }
    }

    override suspend fun delete(domain: Team) {
        db.teamDao().setDelete(domain.uuid)
    }

    override suspend fun update(domain: Team) {
        db.teamDao().update(TeamMapper.toEntity(domain))
    }

    override suspend fun insert(domain: Team) {
        db.teamDao().insert(TeamMapper.toEntity(domain))
    }

    suspend fun updateMembers(domain: Team) {
        domain.run {
            members.forEach { member ->
                when(member.value){
                    DomainState.Insert -> {
                        db.teamMemberDao().insert(
                            TeamMember(
                                userId = member.key.uuid,
                                teamId = this.uuid,
                                role = member.key.roles[this] ?: "member",
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()))

                        members[member.key] = DomainState.Read
                    }

                    DomainState.Delete -> {
                        db.teamMemberDao().setDeleted(member.key.uuid, this.uuid)
                        member.key.isDeleted = true
                        member.key.isSynced = false
                        members[member.key] = DomainState.Read
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }
}