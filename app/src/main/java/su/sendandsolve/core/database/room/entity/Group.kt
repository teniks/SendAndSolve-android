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
    tableName = "groups",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["uuid"],
        childColumns = ["creator_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("uuid"), Index("is_synced"), Index("creator_id")]
)
data class Group(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    val uuid: UUID,

    @ColumnInfo(name = "name_group")
    val name: String,

    @ColumnInfo(name = "is_auto")
    val isAuto: Boolean = false,

    @ColumnInfo(name = "criteria")
    val criteria: JSONObject,

    @ColumnInfo(name = "creator_id")
    val creatorId: UUID,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "data_version")
    val dataVersion: Int = 0,

    @ColumnInfo(name = "last_modified")
    val lastModified: Instant = Instant.now()
)
