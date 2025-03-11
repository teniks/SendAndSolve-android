package su.sendandsolve.data.db.room.util

import androidx.room.TypeConverter
import org.json.JSONObject
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.UUID

class Converters {
    @TypeConverter
    fun fromUuid(value: UUID?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toUuid(value: String?): UUID? {
        return value?.let { UUID.fromString(it) }
    }

    @TypeConverter
    fun fromInstant(value: Instant?): String? {
        return value?.let { DateTimeFormatter.ISO_INSTANT.format(it) }
    }

    @TypeConverter
    fun toInstant(value: String?): Instant? {
        return value?.let { Instant.from(DateTimeFormatter.ISO_INSTANT.parse(it)) }
    }

    @TypeConverter
    fun fromJsonObject(value: JSONObject?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toJsonObject(value: String?): JSONObject? {
        return value?.let { JSONObject(it) }
    }
}