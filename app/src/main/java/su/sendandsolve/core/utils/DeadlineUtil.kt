package su.sendandsolve.core.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DeadlineUtil {
    fun getString(start: Instant, end: Instant?): String{
        val formatter = DateTimeFormatter.ofPattern("HH:mm dd:MM:yyyy")
        val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
        val formatterDate = DateTimeFormatter.ofPattern("dd:MM:yyyy")
        val zoneId = ZoneId.systemDefault()

        val date1 = LocalDateTime.ofInstant(start, zoneId)

        if(end == null){
            return "${date1.format(formatterTime)} / ${date1.format(formatterDate)}"
        }
        val date2 = LocalDateTime.ofInstant(end, zoneId)

        return if (date1.toLocalDate() == date2.toLocalDate()) {
            "${date1.format(formatterTime)} - ${date2.format(formatterTime)} / ${date1.format(formatterDate)}"
        } else {
            "${date1.format(formatter)} - ${date2.format(formatter)}"
        }
    }
}