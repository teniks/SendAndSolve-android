package su.sendandsolve.core.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "task_tags",
    primaryKeys = ["task_id", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["uuid"],
            childColumns = ["task_id"]
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["uuid"],
            childColumns = ["tag_id"]
        )
    ],
    indices = [Index("task_id"), Index("tag_id"), Index("is_synced")]
)
data class TaskTag(
    @ColumnInfo(name = "task_id")
    val taskId: UUID,

    @ColumnInfo(name = "tag_id")
    val tagId: UUID,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "data_version")
    val dataVersion: Int = 0,

    @ColumnInfo(name = "last_modified")
    val lastModified: Instant
)
