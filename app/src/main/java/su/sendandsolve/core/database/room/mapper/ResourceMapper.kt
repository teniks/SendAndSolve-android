package su.sendandsolve.core.database.room.mapper

import su.sendandsolve.core.utils.toJSONObject
import su.sendandsolve.core.utils.toMap
import su.sendandsolve.core.utils.*
import su.sendandsolve.core.database.room.entity.Resource as Entity
import su.sendandsolve.features.tasks.domain.Mapper
import su.sendandsolve.features.tasks.domain.model.Resource as Domain

object ResourceMapper : Mapper<Domain, Entity> {
    override fun toDomain(entity: Entity): Domain {
        val resource = Domain(
            uuid = entity.uuid,
            creatorId = entity.creatorId,
            uploadDate = entity.uploadDate,
            byteSize = entity.byteSize,
            hash = entity.hash,
            filePath = entity.filePath,
            metadata = entity.metadata.toMap(),
            isDeleted = entity.isDeleted,
            isSynced = entity.isSynced,
            dataVersion = entity.dataVersion,
            lastModified = entity.lastModified
        )
        return resource
    }

    override fun toEntity(domain: Domain): Entity {
        val resource = Entity(
            uuid = domain.uuid,
            creatorId = domain.creatorId,
            uploadDate = domain.uploadDate,
            byteSize = domain.byteSize,
            hash = domain.hash,
            filePath = domain.filePath,
            metadata = domain.metadata.toJSONObject(),
            isDeleted = domain.isDeleted,
            isSynced = domain.isSynced,
            dataVersion = domain.dataVersion,
            lastModified = domain.lastModified
        )
        return resource
    }
}


