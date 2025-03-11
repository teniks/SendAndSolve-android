package su.sendandsolve.data.db.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.json.JSONObject
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "resources",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uuid"],
            childColumns = ["creator_id"]
        )
    ],
    indices = [Index("uuid"), Index("is_synced")]
)
data class Resource(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    val uuid: UUID,

    @ColumnInfo(name = "creator_id")
    val creatorId: UUID,

    @ColumnInfo(name = "upload_date")
    val uploadDate: Instant,

    @ColumnInfo(name = "byte_size")
    val byteSize: Long,

    @ColumnInfo(name = "hash")
    val hash: String,

    @ColumnInfo(name = "file_path")
    val filePath: String,

    @ColumnInfo(name = "metadata")
    val metadata: JSONObject,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "data_version")
    val dataVersion: Int = 0,

    @ColumnInfo(name = "last_modified")
    val lastModified: Instant
)
