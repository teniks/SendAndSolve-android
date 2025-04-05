package su.sendandsolve.features.tasks.domain

import java.time.Instant
import java.util.UUID

interface Entity{
    val uuid: UUID
    var isDeleted: Boolean
    var isSynced: Boolean
    var dataVersion: Int
    var lastModified: Instant
}
