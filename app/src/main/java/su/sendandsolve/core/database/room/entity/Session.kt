package su.sendandsolve.core.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "sessions",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uuid"],
            childColumns = ["user_id"]
        ),
        ForeignKey(
            entity = Device::class,
            parentColumns = ["uuid"],
            childColumns = ["device_id"]
        )
    ],
    indices = [Index("uuid"), Index("is_synced"), Index("device_id"), Index("user_id")]
)
data class Session(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    val uuid: UUID,

    @ColumnInfo(name = "user_id")
    val userId: UUID,

    @ColumnInfo(name = "device_id")
    val deviceId: UUID,

    @ColumnInfo(name = "session_token")
    val token: String,

    @ColumnInfo(name = "expiry_date")
    val expiryDate: Instant,

    @ColumnInfo(name = "date_activity")
    val dateActivity: Instant,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "data_version")
    val dataVersion: Int = 0,

    @ColumnInfo(name = "last_modified")
    val lastModified: Instant
    )
