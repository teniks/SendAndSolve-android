package su.sendandsolve.data.domain

import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface Dao<Entity> {
    suspend fun getById(id: UUID, isDeleted: Boolean = false): Entity?
    fun getAll(isDeleted: Boolean = false): Flow<List<Entity>>
    suspend fun insert(entity: Entity)
    suspend fun update(entity: Entity)
    suspend fun setDeleted(entity: UUID, isDeleted: Boolean = false)
}