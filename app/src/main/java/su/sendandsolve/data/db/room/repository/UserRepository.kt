package su.sendandsolve.data.db.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.data.db.room.RoomAppDatabase
import su.sendandsolve.data.db.room.dao.TaskAssignmentDao
import su.sendandsolve.data.db.room.dao.TeamMemberDao
import su.sendandsolve.data.db.room.dao.UserDao
import su.sendandsolve.data.db.room.entity.TaskAssignment
import su.sendandsolve.data.db.room.entity.TeamMember
import su.sendandsolve.data.db.room.mapper.UserMapper
import su.sendandsolve.data.domain.DomainState
import su.sendandsolve.data.domain.Repository
import su.sendandsolve.data.domain.model.User
import java.time.Instant
import java.util.UUID

class UserRepository(
    private val db: RoomAppDatabase
) : Repository<User> {

    override suspend fun getById(id: UUID): User? {
        return db.userDao().getById(id)?.let { UserMapper.toDomain(it) }
    }

    override fun getAll(isDeleted: Boolean): Flow<List<User>> {
        return db.userDao().getDeleted(isDeleted).map { it.map(UserMapper::toDomain) }
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
                        tasks[task.key] = DomainState.Read
                    }

                    DomainState.Delete -> {
                        db.taskAssignmentDao().setDelete(task.key.uuid, this.uuid)
                        task.key.isDeleted = true
                        task.key.isSynced = false
                        tasks[task.key] = DomainState.Read
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
                        teams[team.key] = DomainState.Read
                    }

                    DomainState.Delete -> {
                        db.teamMemberDao().setDeleted(this.uuid, team.key.uuid)
                        team.key.isDeleted = true
                        team.key.isSynced = false
                        teams[team.key] = DomainState.Read
                    }

                    DomainState.Read -> {}
                }
            }
        }
    }
}