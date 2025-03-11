package su.sendandsolve.data.domain.model

import su.sendandsolve.data.domain.DomainState
import su.sendandsolve.data.domain.Entity
import java.time.Instant
import java.util.UUID

data class User (
    override val uuid: UUID,
    var login: String,
    val passwordHash: String,
    val nickname: String,
    var teams: MutableMap<Team, DomainState> = mutableMapOf<Team, DomainState>(),
    var roles: MutableMap<Team, String> = mutableMapOf<Team, String>(),
    var tasks: MutableMap<Task, DomainState> = mutableMapOf<Task, DomainState>(),
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity