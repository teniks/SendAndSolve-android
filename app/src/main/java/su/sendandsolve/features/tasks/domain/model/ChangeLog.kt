package su.sendandsolve.features.tasks.domain.model

import su.sendandsolve.features.tasks.domain.Entity
import java.time.Instant
import java.util.UUID

data class ChangeLog(
    override val uuid: UUID,
    val operationGroupId: UUID,
    val userId: UUID,
    val deviceId: UUID,
    val tableName: String,
    val operationType: String,
    val recordId: UUID,
    val oldValue: Map<String, *>,
    val newValue: Map<String, *>,
    val timestamp: Instant,
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity
