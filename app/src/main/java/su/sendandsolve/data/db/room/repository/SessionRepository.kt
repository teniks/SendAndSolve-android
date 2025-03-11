package su.sendandsolve.data.db.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.data.db.room.RoomAppDatabase
import su.sendandsolve.data.db.room.dao.SessionDao
import su.sendandsolve.data.db.room.mapper.SessionMapper
import su.sendandsolve.data.domain.Repository
import su.sendandsolve.data.domain.model.Session
import java.util.UUID

class SessionRepository(
    private val db: RoomAppDatabase
) : Repository<Session> {
    override suspend fun getById(id: UUID): Session? {
        return db.sessionDao().getById(id)?.let { SessionMapper.toDomain(it) }
    }

    override fun getAll(isDeleted: Boolean): Flow<List<Session>> {
        return db.sessionDao().getDeleted(isDeleted).map { list -> list.map(SessionMapper::toDomain)}
    }

    override suspend fun delete(domain: Session) {
        db.sessionDao().setDeleted(domain.uuid)
    }

    override suspend fun update(domain: Session) {
        db.sessionDao().update(SessionMapper.toEntity(domain))
    }

    override suspend fun insert(domain: Session) {
        db.sessionDao().insert(SessionMapper.toEntity(domain))
    }
}