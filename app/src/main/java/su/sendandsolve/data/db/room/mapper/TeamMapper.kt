package su.sendandsolve.data.db.room.mapper

import su.sendandsolve.data.db.room.entity.Team as Entity
import su.sendandsolve.data.domain.Mapper
import su.sendandsolve.data.domain.model.Team as Domain

object TeamMapper : Mapper<Domain, Entity> {
    override fun toDomain(entity: Entity): Domain {
        val team = Domain(
            uuid = entity.uuid,
            name = entity.name,
            creatorId = entity.creatorId,
            isDeleted = entity.isDeleted,
            isSynced = entity.isSynced,
            dataVersion = entity.dataVersion,
            lastModified = entity.lastModified
        )
        return team
    }

    override fun toEntity(domain: Domain): Entity {
        val team = Entity(
            uuid = domain.uuid,
            name = domain.name,
            creatorId = domain.creatorId,
            isDeleted = domain.isDeleted,
            isSynced = domain.isSynced,
            dataVersion = domain.dataVersion,
            lastModified = domain.lastModified
        )
        return team
    }
}