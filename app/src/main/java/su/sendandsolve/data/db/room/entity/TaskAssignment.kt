package su.sendandsolve.data.db.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "task_assignments",
    primaryKeys = ["task_id", "user_id"],
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["uuid"],
            childColumns = ["task_id"]
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["uuid"],
            childColumns = ["user_id"]
        )
    ],
    indices = [Index("task_id"), Index("user_id"), Index("is_synced")]
)
data class TaskAssignment (
    @ColumnInfo(name = "task_id")
    val taskId: UUID,

    @ColumnInfo(name = "user_id")
    val userId: UUID,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "data_version")
    val dataVersion: Int = 0,

    @ColumnInfo(name = "last_modified")
    val lastModified: Instant
)