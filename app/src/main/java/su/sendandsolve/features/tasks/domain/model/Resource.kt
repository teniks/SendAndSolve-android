package su.sendandsolve.features.tasks.domain.model

import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Entity
import java.time.Instant
import java.util.UUID

data class Resource(
    override val uuid: UUID,
    val creatorId: UUID,
    val uploadDate: Instant,
    val byteSize: Long,
    val hash: String,
    val filePath: String,
    val metadata: Map<String, *>,
    var tasks: MutableMap<Task, DomainState> = mutableMapOf<Task, DomainState>(),
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity
