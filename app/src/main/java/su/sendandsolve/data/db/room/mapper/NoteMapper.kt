package su.sendandsolve.data.db.room.mapper

import su.sendandsolve.data.db.room.entity.Note as Entity
import su.sendandsolve.data.domain.Mapper
import su.sendandsolve.data.domain.model.Note as Domain

object NoteMapper : Mapper<Domain, Entity> {
    override fun toDomain(entity: Entity): Domain {
        val note = Domain(
            uuid = entity.uuid,
            title = entity.title,
            description = entity.description,
            creationDate = entity.creationDate,
            authorId = entity.authorId,
            isDeleted = entity.isDeleted,
            isSynced = entity.isSynced,
            dataVersion = entity.dataVersion,
            lastModified = entity.lastModified
        )
        return note
    }

    override fun toEntity(domain: Domain): Entity {
        val note = su.sendandsolve.data.db.room.entity.Note(
            uuid = domain.uuid,
            title = domain.title,
            description = domain.description,
            creationDate = domain.creationDate,
            authorId = domain.authorId,
            isDeleted = domain.isDeleted,
            isSynced = domain.isSynced,
            dataVersion = domain.dataVersion,
            lastModified = domain.lastModified
        )

        return note
    }
}