package su.sendandsolve.data.db.room.mapper

import su.sendandsolve.data.domain.Mapper
import su.sendandsolve.data.db.room.entity.Task as Entity
import su.sendandsolve.data.domain.model.Task as Domain

object TaskMapper : Mapper<Domain, Entity> {

    override fun toDomain(entity: Entity): Domain {
        val task = Domain(
            uuid = entity.uuid,
            title = entity.title,
            description = entity.description,
            status = entity.status,
            priority = entity.priority,
            startDate = entity.startDate,
            endDate = entity.endDate,
            progress = entity.progress,
            creatorId = entity.creatorId,
            scope = entity.scope,
            teamId = entity.teamId,
            creationDate = entity.creationDate,
            isDeleted = entity.isDeleted,
            isSynced = entity.isSynced,
            dataVersion = entity.dataVersion,
            lastModified = entity.lastModified)


        return task
    }

    override fun toEntity(domain: Domain): Entity {
        val task = Entity(
            uuid = domain.uuid,
            title = domain.title,
            description = domain.description,
            status = domain.status,
            priority = domain.priority,
            startDate = domain.startDate,
            endDate = domain.endDate,
            progress = domain.progress,
            creatorId = domain.creatorId,
            scope = domain.scope,
            teamId = domain.teamId,
            creationDate = domain.creationDate,
            isDeleted = domain.isDeleted,
            isSynced = domain.isSynced,
            dataVersion = domain.dataVersion,
            lastModified = domain.lastModified
        )

        return task
    }
}