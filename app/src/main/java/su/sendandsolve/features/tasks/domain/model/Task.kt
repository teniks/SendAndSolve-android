package su.sendandsolve.features.tasks.domain.model

import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Entity
import java.time.Instant
import java.util.UUID

data class Task(
    override val uuid: UUID,
    val creatorId: UUID,
    val title: String = "",
    val description: String = "",
    val status: String = "",
    val priority: Int = 0,
    val progress: Int = 0,
    val creationDate: Instant = Instant.now(),
    val teamId: UUID? = null,
    val startDate: Instant? = null,
    val endDate: Instant? = null,
    var tags: Map<Tag, DomainState> = emptyMap(),
    var childTasks: Map<Task, DomainState> = emptyMap(),
    var parentTasks: Map<Task, DomainState> = emptyMap(),
    var resources: Map<Resource, DomainState> = emptyMap(),
    var notes: Map<Note, DomainState> = emptyMap(),
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity {

    fun addTag(tag: Tag) = copy(
            tags = tags + (tag to DomainState.Insert)
        )

    fun deleteTag(tag: Tag) = copy(
        tags = tags.toMutableMap().apply {
            this[tag] = DomainState.Delete
        }
    )

    fun addChildTask(task: Task) = copy(
        childTasks = childTasks + (task to DomainState.Insert)
    )

    fun deleteChildTask(task: Task) = copy(
        childTasks = childTasks.toMutableMap().apply {
            this[task] = DomainState.Delete
        }
    )

    fun addParentTask(task: Task) = copy(
        parentTasks = parentTasks + (task to DomainState.Insert)
    )

    fun deleteParentTask(task: Task) = copy(
        parentTasks = parentTasks.toMutableMap().apply {
            this[task] = DomainState.Delete
        }
    )

    fun addResource(resource: Resource) = copy(
        resources = resources + (resource to DomainState.Insert)
    )

    fun deleteResource(resource: Resource) = copy(
        resources = resources.toMutableMap().apply {
            this[resource] = DomainState.Delete
        }
    )

    fun addNote(note: Note) = copy(
        notes = notes + (note to DomainState.Insert)
    )

    fun deleteNote(note: Note) = copy(
        notes = notes.toMutableMap().apply {
            this[note] = DomainState.Delete
        }
    )

    fun getTagsByState(state: DomainState): Map<Tag, DomainState> {
        return tags.filterValues { value -> value == state }
    }

    fun getTagsByNotState(state: DomainState): Map<Tag, DomainState> {
        return tags.filterValues { value -> value != state }
    }

    fun getChildTasksByState(state: DomainState): Map<Task, DomainState> {
        return childTasks.filterValues { value -> value == state }
    }

    fun getChildTasksByNotState(state: DomainState): Map<Task, DomainState> {
        return childTasks.filterValues { value -> value != state }
    }

    fun getParentTasksByState(state: DomainState): Map<Task, DomainState> {
        return parentTasks.filterValues { value -> value == state }
    }

    fun getParentTasksByNotState(state: DomainState): Map<Task, DomainState> {
        return parentTasks.filterValues { value -> value != state }
    }

    fun getResourcesByState(state: DomainState): Map<Resource, DomainState> {
        return resources.filterValues { value -> value == state }
    }

    fun getResourcesByNotState(state: DomainState): Map<Resource, DomainState> {
        return resources.filterValues { value -> value != state }
    }

    fun getNotesByState(state: DomainState): Map<Note, DomainState> {
        return notes.filterValues { value -> value == state }
    }

    fun getNotesByNotState(state: DomainState): Map<Note, DomainState> {
        return notes.filterValues { value -> value != state }
    }
}
