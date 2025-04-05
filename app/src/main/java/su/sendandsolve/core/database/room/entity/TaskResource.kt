package su.sendandsolve.core.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "task_resources",
    primaryKeys = ["task_id", "resource_id"],
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["uuid"],
            childColumns = ["task_id"]
        ),
        ForeignKey(
            entity = Resource::class,
            parentColumns = ["uuid"],
            childColumns = ["resource_id"]
        )
    ],
    indices = [Index("task_id"), Index("resource_id"), Index("is_synced")]
)
data class TaskResource(
    @ColumnInfo(name = "task_id")
    val taskId: UUID,

    @ColumnInfo(name = "resource_id")
    val resourceId: UUID,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "data_version")
    val dataVersion: Int = 0,

    @ColumnInfo(name = "last_modified")
    val lastModified: Instant
)
