package su.sendandsolve.core.database.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.database.room.entity.TeamMember
import su.sendandsolve.core.database.room.mapper.TeamMapper
import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.Team
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class TeamRepository @Inject constructor(
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
                                lastModified = Instant.now())
                        )

                        domain.setReadMember(member.key)
                    }

                    DomainState.Delete -> {
                        db.teamMemberDao().setDeleted(member.key.uuid, this.uuid)
                        db.teamMemberDao().setSynced(member.key.uuid, this.uuid, false)
                        member.key.isDeleted = true
                        member.key.isSynced = false

                        domain.setReadMember(member.key)
                    }

                    DomainState.Recover -> {
                        db.teamMemberDao().setDeleted(member.key.uuid, this.uuid, false)
                        db.teamMemberDao().setSynced(member.key.uuid, this.uuid, false)
                        member.key.isDeleted = false
                        member.key.isSynced = true

                        domain.setReadMember(member.key)
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }
}