package su.sendandsolve.core.database.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.database.room.entity.TaskAssignment
import su.sendandsolve.core.database.room.entity.TeamMember
import su.sendandsolve.core.database.room.mapper.UserMapper
import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.User
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val db: RoomAppDatabase
) : Repository<User> {

    override suspend fun getById(id: UUID): User? {
        return db.userDao().getById(id)?.let { UserMapper.toDomain(it) }
    }

    override fun getAll(isDeleted: Boolean): Flow<List<User>> {
        return db.userDao().getDeleted(isDeleted).map { it.map(UserMapper::toDomain) }
    }

    suspend fun getByLogin(login: String): User? {
        return db.userDao().getByLogin(login)?.let { UserMapper.toDomain(it) }
    }

    override suspend fun delete(domain: User) {
        db.userDao().setDeleted(domain.uuid)
    }

    override suspend fun update(domain: User) {
        db.userDao().update(UserMapper.toEntity(domain))
    }

    override suspend fun insert(domain: User) {
        db.userDao().insert(UserMapper.toEntity(domain))
    }

    suspend fun getCount(login: String): Int {
        return db.userDao().countByLogin(login)
    }

    suspend fun updateTasks(domain: User) {
        domain.run {
            tasks.forEach { task ->
                when (task.value) {
                    DomainState.Insert -> {
                        db.taskAssignmentDao().insert(
                            TaskAssignment(
                                userId = this.uuid,
                                taskId = task.key.uuid,
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )

                        domain.setReadTask(task.key)
                    }

                    DomainState.Delete -> {
                        db.taskAssignmentDao().setDelete(task.key.uuid, this.uuid)
                        db.taskAssignmentDao().setSynced(task.key.uuid, this.uuid, false)
                        task.key.isDeleted = true
                        task.key.isSynced = false

                        domain.deleteTask(task.key)
                    }

                    DomainState.Recover -> {
                        db.taskAssignmentDao().setDelete(task.key.uuid, this.uuid, false)
                        db.taskAssignmentDao().setSynced(task.key.uuid, this.uuid, false)
                        task.key.isDeleted = false
                        task.key.isSynced = true

                        domain.setReadTask(task.key)
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }

    suspend fun updateTeams(domain: User) {
        domain.run {
            teams.forEach { team ->
                when (team.value) {
                    DomainState.Insert -> {
                        db.teamMemberDao().insert(
                            TeamMember(
                                userId = this.uuid,
                                teamId = team.key.uuid,
                                role = this.roles[team.key] ?: "member",
                                isDeleted = false,
                                isSynced = false,
                                dataVersion = 0,
                                lastModified = Instant.now()
                            )
                        )

                        domain.setReadTeam(team.key)
                    }

                    DomainState.Delete -> {
                        db.teamMemberDao().setDeleted(this.uuid, team.key.uuid)
                        db.teamMemberDao().setSynced(this.uuid, team.key.uuid, false)
                        team.key.isDeleted = true
                        team.key.isSynced = false

                        domain.deleteTeam(team.key)
                    }

                    DomainState.Recover -> {
                        db.teamMemberDao().setDeleted(this.uuid, team.key.uuid, false)
                        db.teamMemberDao().setSynced(this.uuid, team.key.uuid, false)
                        team.key.isDeleted = false
                        team.key.isSynced = true

                        domain.setReadTeam(team.key)
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }
}