package su.sendandsolve.core.database.room.mapper

import su.sendandsolve.core.database.room.entity.Device as Entity
import su.sendandsolve.features.tasks.domain.model.Device as Domain
import su.sendandsolve.features.tasks.domain.Mapper

object DeviceMapper : Mapper<Domain, Entity> {
    override fun toDomain(entity: Entity): Domain {
        val device = Domain(
            uuid = entity.uuid,
            name = entity.name,
            lastSeen = entity.lastSeen,
            isDeleted = entity.isDeleted,
            isSynced = entity.isSynced,
            dataVersion = entity.dataVersion,
            lastModified = entity.lastModified
        )
        return device
    }

    override fun toEntity(domain: Domain): Entity {
        val device = Entity(
            uuid = domain.uuid,
            name = domain.name,
            lastSeen = domain.lastSeen,
            isDeleted = domain.isDeleted,
            isSynced = domain.isSynced,
            dataVersion = domain.dataVersion,
            lastModified = domain.lastModified
        )
        return device
    }

}