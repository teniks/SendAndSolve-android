package su.sendandsolve.core.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "notes",
    indices = [Index("uuid"), Index("is_synced")]
)
data class Note(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    val uuid: UUID,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "date_creation")
    val creationDate: Instant,

    @ColumnInfo(name = "author_id")
    val authorId: UUID,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "data_version")
    val dataVersion: Int = 0,

    @ColumnInfo(name = "last_modified")
    val lastModified: Instant
)
