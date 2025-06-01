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
    var tags: Map<Tag, DomainState> = emptyMap(),
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

    fun addTag(tag: Tag) = copy(
        tags = tags + (tag to DomainState.Insert))

    fun deleteTag(tag: Tag) = copy(
        tags = tags.toMutableMap().apply {
            this[tag] = DomainState.Delete })

    fun setReadTag(tag: Tag) = copy(
        tags = tags.toMutableMap().apply {
            this[tag] = DomainState.Read })

    fun getTasksByState(state: DomainState) =
        tasks.filterValues { value -> value == state }

    fun getTasksByNotState(state: DomainState) =
        tasks.filterValues { value -> value != state }

    fun getTagsByState(state: DomainState) =
        tasks.filterValues { value -> value == state }

    fun getTagsByNotState(state: DomainState) =
        tasks.filterValues { value -> value != state }
}
