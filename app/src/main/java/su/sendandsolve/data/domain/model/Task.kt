package su.sendandsolve.data.domain.model

import su.sendandsolve.data.domain.DomainState
import su.sendandsolve.data.domain.Entity
import java.time.Instant
import java.util.UUID

data class Task(
    override val uuid: UUID,
    val title: String,
    val description: String,
    val status: String,
    val priority: Int,
    val startDate: Instant,
    val endDate: Instant? = null,
    val progress: Int,
    val creatorId: UUID,
    val scope: String,
    val teamId: UUID? = null,
    val creationDate: Instant,
    var tags: MutableMap<Tag, DomainState> = mutableMapOf<Tag, DomainState>(),
    var childTasks: MutableMap<Task, DomainState> = mutableMapOf<Task, DomainState>(),
    var parentTasks: MutableMap<Task, DomainState> = mutableMapOf<Task, DomainState>(),
    var resources: MutableMap<Resource, DomainState> = mutableMapOf<Resource, DomainState>(),
    var notes: MutableMap<Note, DomainState> = mutableMapOf<Note, DomainState>(),
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity
