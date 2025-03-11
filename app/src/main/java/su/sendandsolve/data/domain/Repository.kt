package su.sendandsolve.data.domain

import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface Repository<Domain> {
    suspend fun getById(id: UUID): Domain?
    fun getAll(isDeleted: Boolean = false): Flow<List<Domain>>
    suspend fun insert(domain: Domain)
    suspend fun update(domain: Domain)
    suspend fun delete(domain: Domain)
}