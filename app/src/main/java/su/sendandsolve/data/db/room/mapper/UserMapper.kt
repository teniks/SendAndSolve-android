package su.sendandsolve.data.db.room.mapper

import su.sendandsolve.data.db.room.entity.User as Entity
import su.sendandsolve.data.domain.Mapper
import su.sendandsolve.data.domain.model.User as Domain

object UserMapper : Mapper<Domain, Entity> {
    override fun toDomain(entity: Entity): Domain {
        val user = Domain(
            uuid = entity.uuid,
            nickname = entity.nickname,
            login = entity.login,
            passwordHash = entity.passwordHash,
            isDeleted = entity.isDeleted,
            isSynced = entity.isSynced,
            dataVersion = entity.dataVersion,
            lastModified = entity.lastModified
        )
        return user
    }

    override fun toEntity(domain: Domain): Entity {
        val user = Entity(
            uuid = domain.uuid,
            login = domain.login,
            passwordHash = domain.passwordHash,
            nickname = domain.nickname,
            isDeleted = domain.isDeleted,
            isSynced = domain.isSynced,
            dataVersion = domain.dataVersion,
            lastModified = domain.lastModified
        )
        return user
    }

}