package su.sendandsolve.features.tasks.domain.model

import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Entity
import java.time.Instant
import java.util.UUID

data class Group(
    override val uuid: UUID,
    val name: String,
    val creatorId: UUID,
    val isAuto: Boolean = false,
    val criteria: Map<String, *>,
    val tasks: Map<Task, DomainState> = emptyMap(),
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity {

    fun addTask(task: Task) = copy(
        tasks = tasks + (task to DomainState.Insert))

    fun deleteTask(task: Task) = copy(
        tasks = tasks.toMutableMap().apply {
            this[task] = DomainState.Delete
        })

    fun setReadTask(task: Task) = copy(
        tasks = tasks.toMutableMap().apply {
            this[task] = DomainState.Read
        })

    fun getTasksByState(state: DomainState) =
        tasks.filterValues { value -> value == state }

    fun getTasksByNotState(state: DomainState) =
        tasks.filterValues { value -> value != state }
}