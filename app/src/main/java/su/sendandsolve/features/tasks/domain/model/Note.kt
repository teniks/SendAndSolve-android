package su.sendandsolve.features.tasks.domain.model

import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Entity
import java.time.Instant
import java.util.UUID

data class Note(
    override val uuid: UUID,
    val title: String,
    val description: String,
    val authorId: UUID,
    val creationDate: Instant,
    var tags: MutableMap<Tag, DomainState> = mutableMapOf<Tag, DomainState>(),
    var tasks: MutableMap<Task, DomainState> = mutableMapOf<Task, DomainState>(),
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity
