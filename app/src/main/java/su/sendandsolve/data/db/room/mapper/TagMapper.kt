package su.sendandsolve.data.db.room.mapper

import su.sendandsolve.data.db.room.entity.Tag as Entity
import su.sendandsolve.data.domain.Mapper
import su.sendandsolve.data.domain.model.Tag as Domain

object TagMapper : Mapper<Domain, Entity> {
    override fun toDomain(entity: Entity): Domain {
        val tag = Domain(
            uuid = entity.uuid,
            name = entity.tag,
            isDeleted = entity.isDeleted,
            isSynced = entity.isSynced,
            dataVersion = entity.dataVersion,
            lastModified = entity.lastModified
        )
        return tag
    }

    override fun toEntity(domain: Domain): Entity {
        val tag = Entity(
            uuid = domain.uuid,
            tag = domain.name,
            isDeleted = domain.isDeleted,
            isSynced = domain.isSynced,
            dataVersion = domain.dataVersion,
            lastModified = domain.lastModified
        )
        return tag
    }

}