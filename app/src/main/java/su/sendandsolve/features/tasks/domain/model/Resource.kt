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
    var tasks: Map<Task, DomainState> = emptyMap(),
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity {

    fun addTask(task: Task) = copy(
        tasks = tasks + (task to DomainState.Insert))

    fun deleteTask(task: Task) = copy(
        tasks = tasks.toMutableMap().apply {
            this[task] = DomainState.Delete })

    fun setReadTask(task: Task) = copy(
        tasks = tasks.toMutableMap().apply {
            this[task] = DomainState.Read })

    fun getTasksByState(state: DomainState) =
        tasks.filterValues { value -> value == state }

    fun getTasksByNotState(state: DomainState) =
        tasks.filterValues { value -> value != state }
}
