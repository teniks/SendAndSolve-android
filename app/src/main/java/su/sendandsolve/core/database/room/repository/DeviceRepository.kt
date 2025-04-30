package su.sendandsolve.core.database.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.database.room.mapper.DeviceMapper
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.Device
import java.util.UUID
import javax.inject.Inject

class DeviceRepository @Inject constructor(
    private val db: RoomAppDatabase
) : Repository<Device> {
    override suspend fun getById(id: UUID): Device? {
        return db.deviceDao().getById(id)?.let { DeviceMapper.toDomain(it) }
    }

    override fun getAll(isDeleted: Boolean): Flow<List<Device>> {
        return db.deviceDao().getDeleted(isDeleted).map { list -> list.map(DeviceMapper::toDomain) }
    }

    override suspend fun delete(domain: Device) {
        db.deviceDao().setDelete(domain.uuid)
    }

    override suspend fun update(domain: Device) {
        db.deviceDao().update(DeviceMapper.toEntity(domain))
    }

    override suspend fun insert(domain: Device) {
        db.deviceDao().insert(DeviceMapper.toEntity(domain))
    }

}