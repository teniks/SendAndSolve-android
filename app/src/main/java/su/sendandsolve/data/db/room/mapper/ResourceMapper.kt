package su.sendandsolve.data.db.room.mapper

import su.sendandsolve.utils.*
import su.sendandsolve.data.db.room.entity.Resource as Entity
import su.sendandsolve.data.domain.Mapper
import su.sendandsolve.data.domain.model.Resource as Domain

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


