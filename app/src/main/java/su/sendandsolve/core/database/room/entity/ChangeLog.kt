package su.sendandsolve.core.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.json.JSONObject
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "changelogs",
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
    indices = [Index("uuid"), Index("is_synced"), Index("user_id"), Index("timestamp"), Index("device_id")]
)
data class ChangeLog(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    val uuid: UUID,

    @ColumnInfo(name = "operation_group_id")
    val operationGroupId: UUID,

    @ColumnInfo(name = "user_id")
    val userId: UUID,

    @ColumnInfo(name = "device_id")
    val deviceId: UUID,

    @ColumnInfo(name = "table_name")
    val tableName: String,

    @ColumnInfo(name = "operation_type")
    val operationType: String,

    @ColumnInfo(name = "record_id")
    val recordId: UUID,

    @ColumnInfo(name = "old_value")
    val oldValue: JSONObject,

    @ColumnInfo(name = "new_value")
    val newValue: JSONObject,

    @ColumnInfo(name = "timestamp")
    val timestamp: Instant,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "data_version")
    val dataVersion: Int = 0,

    @ColumnInfo(name = "last_modified")
    val lastModified: Instant
)
