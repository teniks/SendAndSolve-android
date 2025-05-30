package su.sendandsolve.features.tasks.domain.enums

import android.content.Context
import su.sendandsolve.R

/**
 * CRITICAL - important and urgent.
 * STRATEGIC - important, but not urgent.
 * TACTICAL - urgent, but not important.
 * BACKGROUND - neither important, nor urgent.
 * @author: teniks
 * @since: 01.05.2025
 */
enum class TaskPriority(private val priorityId: Int) {
    CRITICAL(R.string.priority_CRITICAL),
    STRATEGIC(R.string.priority_STRATEGIC),
    TACTICAL(R.string.priority_TACTICAL),
    BACKGROUND(R.string.priority_BACKGROUND);

    fun getString(context: Context): String = context.getString(priorityId)

    companion object {
        fun getPriorityList(context: Context): List<Pair<TaskPriority, String>> {
            return entries.map {
                Pair(it, it.getString(context))
            }
        }
    }
}
