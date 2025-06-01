package su.sendandsolve.features.tasks.domain.model

import su.sendandsolve.features.tasks.domain.DomainState
import su.sendandsolve.features.tasks.domain.Entity
import java.time.Instant
import java.util.UUID

data class Team(
    override val uuid: UUID,
    val name: String,
    val creatorId: UUID,
    var members: Map<User, DomainState> = emptyMap(),
    override var isDeleted: Boolean = false,
    override var isSynced: Boolean = false,
    override var dataVersion: Int = 0,
    override var lastModified: Instant = Instant.now()
) : Entity {

    fun addMember(user: User) = copy(
        members = members + (user to DomainState.Insert)
    )

    fun deleteMember(user: User) = copy(
        members = members.toMutableMap().apply {
            this[user] = DomainState.Delete
        })

    fun setReadMember(user: User) = copy(
        members = members.toMutableMap().apply {
            this[user] = DomainState.Read
        }
    )

    fun getMemberByState(state: DomainState) =
        members.filterValues { it == state }

    fun getMemberByNotState(state: DomainState) =
        members.filterValues { it != state }
}
