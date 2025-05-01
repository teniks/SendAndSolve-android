package su.sendandsolve.features.tasks.detail.ui.tags

import su.sendandsolve.features.tasks.domain.model.Tag
import java.util.UUID

data class TagsDialogState(
    val tags: List<Tag> = emptyList(),
    val selectedTagIds: Set<UUID> = emptySet()
)
