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
    var teams: MutableMap<Team, DomainState> = mutableMapOf<Team, DomainState>(),
    var roles: MutableMap<Team, String> = mutableMapOf<Team, String>(),
    var tasks: MutableMap<Task, DomainState> = mutableMapOf<Task, DomainState>(),
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity