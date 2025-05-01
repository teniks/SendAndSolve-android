package su.sendandsolve.core.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uuid"],
            childColumns = ["creator_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("uuid"), Index("is_synced"), Index("creator_id"), Index("status"), Index("priority"), Index("start_date"), Index("end_date"), Index("scope")]
)
data class Task(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    val uuid: UUID,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "priority")
    val priority: Int,

    @ColumnInfo(name = "start_date")
    val startDate: Instant? = null,

    @ColumnInfo(name = "end_date")
    val endDate: Instant? = null,

    @ColumnInfo(name = "progress")
    val progress: Int,

    @ColumnInfo(name = "creator_id")
    val creatorId: UUID,

    @ColumnInfo(name = "team_id")
    val teamId: UUID? = null,

    @ColumnInfo(name = "date_creation")
    val creationDate: Instant,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "data_version")
    val dataVersion: Int = 0,

    @ColumnInfo(name = "last_modified")
    val lastModified: Instant
)
