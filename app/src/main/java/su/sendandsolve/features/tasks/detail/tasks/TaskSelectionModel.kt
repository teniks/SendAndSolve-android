package su.sendandsolve.features.tasks.detail.tasks

import su.sendandsolve.features.tasks.domain.model.Task

data class TaskSelectionModel(
    val task: Task,
    var isSelected: Boolean = false
)