package su.sendandsolve.features.tasks.domain.model

import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Entity
import java.time.Instant
import java.util.UUID

data class User (
    override val uuid: UUID,
    var login: String,
    val passwordHash: String,
    val salt: String,
    val nickname: String,
    val imagePath: String? = null,
    var teams: Map<Team, DomainState> = emptyMap(),
    var roles: Map<Team, String> = emptyMap(),
    var tasks: Map<Task, DomainState> = emptyMap(),
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity {

    fun addTeam(team: Team) = copy(
        teams = teams + (team to DomainState.Insert)
    )

    fun deleteTeam(team: Team) = copy(
        teams = teams.toMutableMap().apply {
            this[team] = DomainState.Delete
        },
        roles = roles.toMutableMap().apply {
            this.remove(team)
        }
    )

    fun setReadTeam(team: Team) = copy(
        teams = teams.toMutableMap().apply {
            this[team] = DomainState.Read
        }
    )

    fun getTeamsByState(state: DomainState) =
        teams.filterValues { value -> value == state }

    fun getTeamsByNotState(state: DomainState) =
        teams.filterValues { value -> value != state }

    fun addRole(team: Team, role: String) = copy(
        roles = roles + (team to role)
    )

    fun deleteRole(team: Team) = copy(
        roles = roles.toMutableMap().apply {
            this.remove(team)
        }
    )

    fun setReadRole(team: Team, role: String) = copy(
        roles = roles.toMutableMap().apply {
            this[team] = role
        }
    )

    fun getRolesByRole(role: String) =
        roles.filterValues { value -> value == role }

    fun getRolesByNotState(role: String) =
        roles.filterValues { value -> value != role }

    fun addTask(task: Task) = copy(
        tasks = tasks + (task to DomainState.Insert)
    )

    fun deleteTask(task: Task) = copy(
        tasks = tasks.toMutableMap().apply {
            this[task] = DomainState.Delete
        }
    )

    fun setReadTask(task: Task) = copy(
        tasks = tasks.toMutableMap().apply {
            this[task] = DomainState.Read
        }
    )

    fun getTasksByState(state: DomainState) =
        tasks.filterValues { value -> value == state }

    fun getTasksByNotState(state: DomainState) =
        tasks.filterValues { value -> value != state }
}