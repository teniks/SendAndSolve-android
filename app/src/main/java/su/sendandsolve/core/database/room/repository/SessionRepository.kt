package su.sendandsolve.core.database.room.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import su.sendandsolve.core.database.room.RoomAppDatabase
import su.sendandsolve.core.database.room.mapper.SessionMapper
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.Session
import java.util.UUID
import javax.inject.Inject

class SessionRepository @Inject constructor(
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