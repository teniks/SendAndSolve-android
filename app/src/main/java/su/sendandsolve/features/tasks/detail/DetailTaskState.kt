package su.sendandsolve.features.tasks.detail

import su.sendandsolve.features.tasks.domain.model.Task

data class DetailTaskState(
    val task: Task? = null,
    val error: String? = null,
    val startDateError: String? = null,
    val endDateError: String? = null
)

sealed class DetailTaskEvent {
    data class TaskChanged(val update: Task.() -> Task) : DetailTaskEvent()
    data object Submit : DetailTaskEvent()
}
