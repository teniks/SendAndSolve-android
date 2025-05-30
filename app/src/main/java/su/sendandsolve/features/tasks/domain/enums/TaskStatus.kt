package su.sendandsolve.features.tasks.domain.enums

import android.content.Context
import su.sendandsolve.R

enum class TaskStatus(private val statusId: Int) {
    NEW(R.string.status_NEW),
    IN_PROGRESS(R.string.status_IN_PROGRESS),
    ON_HOLD(R.string.status_ON_HOLD),
    COMPLETED(R.string.status_COMPLETED),
    CANCELLED(R.string.status_CANCELLED);

    fun getStatus(context: Context) = context.getString(statusId)

    companion object {
        fun getStatusList(context: Context): List<Pair<TaskStatus, String>> {
            return entries.map {
                Pair(it, it.getStatus(context)) }
        }
    }
}