package su.sendandsolve.data.domain

interface Mapper <Domain, Entity> {
    fun toDomain(entity: Entity): Domain
    fun toEntity(entity: Domain): Entity
}