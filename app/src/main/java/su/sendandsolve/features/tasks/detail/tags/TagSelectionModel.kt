package su.sendandsolve.features.tasks.detail.tags

import su.sendandsolve.features.tasks.domain.model.Tag

data class TagSelectionModel(
    val tag: Tag,
    var isSelected: Boolean = false
)
