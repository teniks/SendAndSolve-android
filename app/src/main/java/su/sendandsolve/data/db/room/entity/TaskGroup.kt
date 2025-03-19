package su.sendandsolve.data.db.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "task_groups",
    primaryKeys = ["group_id", "task_id"],
    foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = ["uuid"],
            childColumns = ["group_id"]
        ),
        ForeignKey(
            entity = Task::class,
            parentColumns = ["uuid"],
            childColumns = ["task_id"]
        )],
    indices = [Index("group_id"), Index("task_id"), Index("is_synced")]
    )
data class TaskGroup(
    @ColumnInfo(name = "group_id")
    val groupId: UUID,

    @ColumnInfo(name = "task_id")
    val taskId: UUID,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "data_version")
    val dataVersion: Int = 0,

    @ColumnInfo(name = "last_modified")
    val lastModified: Instant = Instant.now()
)
