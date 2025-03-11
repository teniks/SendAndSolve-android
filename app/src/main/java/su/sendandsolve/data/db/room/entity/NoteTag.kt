package su.sendandsolve.data.db.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "note_tags",
    primaryKeys = ["note_id", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = ["uuid"],
            childColumns = ["note_id"]
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["uuid"],
            childColumns = ["tag_id"]
        )
    ],
    indices = [Index("note_id"), Index("tag_id"), Index("is_synced")]
)
data class NoteTag(
    @ColumnInfo(name = "note_id")
    val noteId: UUID,

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