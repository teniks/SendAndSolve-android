package su.sendandsolve.features.tasks.domain.model

import su.sendandsolve.features.tasks.domain.Entity
import java.time.Instant
import java.util.UUID

data class Session(
    override val uuid: UUID,
    val userId: UUID,
    val token: String,
    val expiryDate: Instant,
    val lastActivity: Instant,
    val deviceId: UUID,
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity
