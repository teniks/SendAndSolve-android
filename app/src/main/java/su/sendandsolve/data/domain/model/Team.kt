package su.sendandsolve.data.domain.model

import su.sendandsolve.data.domain.DomainState
import su.sendandsolve.data.domain.Entity
import java.time.Instant
import java.util.UUID

data class Team(
    override val uuid: UUID,
    val name: String,
    val creatorId: UUID,
    var members: MutableMap<User, DomainState> = mutableMapOf<User, DomainState>(),
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity
