package su.sendandsolve.data.domain.model

import su.sendandsolve.data.domain.DomainState
import su.sendandsolve.data.domain.Entity
import java.time.Instant
import java.util.UUID

data class Group(
    override val uuid: UUID,
    val name: String,
    val isAuto: Boolean = false,
    val criteria: Map<String, *>,
    val creatorId: UUID,
    val tasks: MutableMap<Task, DomainState> = mutableMapOf(),
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity