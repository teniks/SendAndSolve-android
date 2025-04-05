package su.sendandsolve.core.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "team_members",
    primaryKeys = ["team_id", "user_id"],
    foreignKeys = [
        ForeignKey(
            entity = Team::class,
            parentColumns = ["uuid"],
            childColumns = ["team_id"]
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["uuid"],
            childColumns = ["user_id"]
        )
    ],
    indices = [Index("user_id"), Index("team_id"), Index("is_synced")]
)
data class TeamMember(
    @ColumnInfo(name = "team_id")
    val teamId: UUID,

    @ColumnInfo(name = "user_id")
    val userId: UUID,

    @ColumnInfo(name = "team_role")
    val role: String,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "data_version")
    val dataVersion: Int = 0,

    @ColumnInfo(name = "last_modified")
    val lastModified: Instant
)
