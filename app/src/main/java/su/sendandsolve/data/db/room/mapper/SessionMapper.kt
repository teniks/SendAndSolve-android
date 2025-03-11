package su.sendandsolve.data.db.room.mapper

import su.sendandsolve.data.db.room.entity.Session as Entity
import su.sendandsolve.data.domain.Mapper
import su.sendandsolve.data.domain.model.Session as Domain

object SessionMapper : Mapper<Domain, Entity> {
    override fun toDomain(entity: Entity): Domain {
        val session = Domain(
            uuid = entity.uuid,
            userId = entity.userId,
            token = entity.token,
            expiryDate = entity.dateActivity,
            lastActivity = entity.dateActivity,
            deviceId = entity.deviceId,
            isDeleted = entity.isDeleted,
            isSynced = entity.isSynced,
            dataVersion = entity.dataVersion,
            lastModified = entity.lastModified
        )
        return session
    }

    override fun toEntity(domain: Domain): Entity {
        val session = Entity(
            uuid = domain.uuid,
            userId = domain.userId,
            deviceId = domain.deviceId,
            token = domain.token,
            expiryDate = domain.expiryDate,
            dateActivity = domain.lastActivity,
            isDeleted = domain.isDeleted,
            isSynced = domain.isSynced,
            dataVersion = domain.dataVersion,
            lastModified = domain.lastModified
        )
        return session
    }
}