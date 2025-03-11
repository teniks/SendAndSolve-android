package su.sendandsolve.data.db.room.mapper

import su.sendandsolve.data.db.room.entity.ChangeLog as Entity
import su.sendandsolve.data.domain.model.ChangeLog as Domain
import su.sendandsolve.data.domain.Mapper
import su.sendandsolve.util.toJSONObject
import su.sendandsolve.util.toMap

object ChangeLogMapper : Mapper<Domain, Entity> {
    override fun toDomain(entity: Entity): Domain {
        val changeLog = Domain(
            uuid = entity.uuid,
            operationGroupId = entity.operationGroupId,
            userId = entity.userId,
            deviceId = entity.deviceId,
            tableName = entity.tableName,
            operationType = entity.operationType,
            recordId = entity.recordId,
            oldValue = entity.oldValue.toMap(),
            newValue = entity.newValue.toMap(),
            timestamp = entity.timestamp,
            isDeleted = entity.isDeleted,
            isSynced = entity.isSynced,
            dataVersion = entity.dataVersion,
            lastModified = entity.lastModified
        )
        return changeLog
    }

    override fun toEntity(domain: Domain): Entity {
        val changeLog = Entity(
            uuid = domain.uuid,
            operationGroupId = domain.operationGroupId,
            userId = domain.userId,
            deviceId = domain.deviceId,
            tableName = domain.tableName,
            operationType = domain.operationType,
            recordId = domain.recordId,
            oldValue = domain.oldValue.toJSONObject(),
            newValue = domain.newValue.toJSONObject(),
            timestamp = domain.timestamp,
            dataVersion = domain.dataVersion,
            lastModified = domain.lastModified
        )
        return changeLog
    }
}