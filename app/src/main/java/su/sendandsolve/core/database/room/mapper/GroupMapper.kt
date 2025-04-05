package su.sendandsolve.core.database.room.mapper

import su.sendandsolve.features.tasks.domain.Mapper
import su.sendandsolve.core.utils.toJSONObject
import su.sendandsolve.core.utils.toMap
import su.sendandsolve.core.database.room.entity.Group as Entity
import su.sendandsolve.features.tasks.domain.model.Group as Domain

object GroupMapper : Mapper<Domain, Entity> {
    override fun toDomain(entity: Entity): Domain {
        val group = Domain(
            uuid = entity.uuid,
            name = entity.name,
            isAuto = entity.isAuto,
            criteria = entity.criteria.toMap(),
            creatorId = entity.creatorId,
            isDeleted = entity.isDeleted,
            isSynced = entity.isSynced,
            dataVersion = entity.dataVersion,
            lastModified = entity.lastModified
        )
        return group
    }

    override fun toEntity(domain: Domain): Entity {
        val group = Entity(
            uuid = domain.uuid,
            name = domain.name,
            isAuto = domain.isAuto,
            criteria = domain.criteria.toJSONObject(),
            creatorId = domain.creatorId,
            isDeleted = domain.isDeleted,
            isSynced = domain.isSynced,
            dataVersion = domain.dataVersion,
            lastModified = domain.lastModified)
        return group
    }
}