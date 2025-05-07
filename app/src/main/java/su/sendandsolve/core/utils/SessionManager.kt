package su.sendandsolve.core.utils

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object PrefKeys {
        const val FILE = "su.sendandsolve.SESSION"
        const val MODE = Context.MODE_PRIVATE
        const val USER_ID = "user_id"
    }

    private val prefs = context.getSharedPreferences(FILE, MODE)

    suspend fun getCurrentUser(): UUID? {
        val userIdString: String = prefs.getString(USER_ID, "").toString()
        return if (userIdString.isEmpty()) {
            null
        } else {
            try {
                UUID.fromString(userIdString)
            } catch (e: IllegalArgumentException) {
                Log.e("SessionManager", "Invalid user ID string: $userIdString", e)
                null
            }
        }
    }

    suspend fun setCurrentUser(userId: UUID) {
        with(prefs.edit()) {
            putString(USER_ID, userId.toString())
            apply()
        }
    }

    suspend fun logout() {
        prefs.edit().remove(USER_ID).apply()
    }
}