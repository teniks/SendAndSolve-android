package su.sendandsolve.data.db.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.data.db.room.RoomAppDatabase
import su.sendandsolve.data.db.room.dao.DeviceDao
import su.sendandsolve.data.db.room.mapper.DeviceMapper
import su.sendandsolve.data.domain.model.Device
import su.sendandsolve.data.domain.Repository
import java.util.UUID

class DeviceRepository(
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