package su.sendandsolve.features.tasks.detail.tags

import su.sendandsolve.features.tasks.domain.model.Tag

data class TagsDialogState(
    val tags: List<Tag> = emptyList(),
    val selectedTags: Set<Tag> = emptySet()
)
