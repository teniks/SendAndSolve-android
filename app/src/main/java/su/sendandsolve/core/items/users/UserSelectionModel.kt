package su.sendandsolve.core.items.users

import su.sendandsolve.features.tasks.domain.model.User

data class UserSelectionModel(
    val user: User,
    var isSelected: Boolean = false
)
