package su.sendandsolve.features.tasks.domain.model

import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Entity
import java.time.Instant
import java.util.UUID

data class Tag(
    override val uuid: UUID,
    val name: String,
    var tasks: Map<Task, DomainState> = emptyMap(),
    var notes: Map<Note, DomainState> = emptyMap(),
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

    fun addNote(note: Note) = copy(
        notes = notes + (note to DomainState.Insert))

    fun deleteNote(note: Note) = copy(
        notes = notes.toMutableMap().apply {
            this[note] = DomainState.Delete })

    fun setReadNote(note: Note) = copy(
        notes = notes.toMutableMap().apply {
            this[note] = DomainState.Read })

    fun getTasksByState(state: DomainState) =
        tasks.filterValues { it == state }

    fun getTasksByNotState(state: DomainState) =
        tasks.filterValues { it != state }

    fun getNotesByState(state: DomainState) =
        notes.filterValues { it == state }

    fun getNotesByNotState(state: DomainState) =
        notes.filterValues { it != state }
}
