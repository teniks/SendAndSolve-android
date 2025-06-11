package su.sendandsolve.core.utils

import android.content.Context
import android.os.Build
import android.os.FileObserver
import androidx.annotation.RequiresApi
import su.sendandsolve.R
import java.io.File

@RequiresApi(Build.VERSION_CODES.Q)
class SecurityFileObserver(private val context: Context, file: File, private val criticalEvents: Set<Int> = emptySet(), private val securityLockdown: () -> Unit): FileObserver(file) {

    companion object {
        const val TAG = "SecurityMonitor"
        const val SECURITY_LOG_FILENAME = "security_audit.log"
    }

    override fun onEvent(event: Int, path: String?) {
        val message: String = when(event) {
            OPEN -> "${context.getString(R.string.trying_to_open_the_file)} $path"
            ACCESS -> "${context.getString(R.string.unauthorized_file_access)} $path"
            MODIFY -> "${context.getString(R.string.modification_of_the_file)} $path"
            DELETE -> "${context.getString(R.string.deleting_the_file)} $path"
            else -> return
        }

        if (criticalEvents.contains(event)) {
            logEvent("${context.getString(R.string.critical_event)} : $message")
        } else {
            logEvent(message)
        }
    }

    private fun logEvent(message: String) {
        // Запись в лог безопасности
        context.openFileOutput(SECURITY_LOG_FILENAME, Context.MODE_APPEND).use { stream ->
            stream.write("${System.currentTimeMillis()}: $TAG : $message\n".toByteArray())
        }

        if (message.contains(context.getString(R.string.critical_event))) {
            triggerSecurityLockdown()
        }
    }

    private fun triggerSecurityLockdown() {
        // Блокировка приложения и запуск дополнительной блокировки безопасности
        val prefs = context.getSharedPreferences("security", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("lockdown", true).apply()

        securityLockdown()
    }
}