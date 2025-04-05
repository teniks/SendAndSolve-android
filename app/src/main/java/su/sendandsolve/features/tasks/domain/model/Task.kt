package su.sendandsolve.features.tasks.domain.model

import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Entity
import java.time.Instant
import java.util.UUID

data class Task(
    override val uuid: UUID,
    val title: String,
    val description: String,
    val status: String,
    val priority: Int,
    val startDate: Instant,
    val progress: Int,
    val creatorId: UUID,
    val scope: String,
    val creationDate: Instant,
    val teamId: UUID? = null,
    val endDate: Instant? = null,
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
