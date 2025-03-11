package su.sendandsolve.data.db.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "task_hierarchy",
    primaryKeys = ["parent_id", "child_id"],
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["uuid"],
            childColumns = ["parent_id"]
        ),
        ForeignKey(
            entity = Task::class,
            parentColumns = ["uuid"],
            childColumns = ["child_id"]
        )
    ],
    indices = [Index("parent_id"), Index("child_id"), Index("is_synced")]
)
data class TaskHierarchy(
    @ColumnInfo(name = "parent_id")
    val parentId: UUID,

    @ColumnInfo(name = "child_id")
    val childId: UUID,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "data_version")
    val dataVersion: Int = 0,

    @ColumnInfo(name = "last_modified")
    val lastModified: Instant
)
