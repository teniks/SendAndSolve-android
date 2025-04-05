package su.sendandsolve.features.tasks.domain.model

import su.sendandsolve.features.tasks.domain.Entity
import java.time.Instant
import java.util.UUID

data class Device(
    override val uuid: UUID,
    val name: String,
    val lastSeen: Instant,
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity
