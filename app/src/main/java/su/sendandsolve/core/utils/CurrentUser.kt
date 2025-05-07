package su.sendandsolve.core.utils

import java.util.UUID
import javax.inject.Inject

class CurrentUser @Inject constructor(
    private val sessionManager: SessionManager
) {

    suspend fun getUserId(): UUID? {
        return sessionManager.getCurrentUser()
    }

    suspend fun setCurrentUser(currentUserId: UUID) {
        sessionManager.setCurrentUser(currentUserId)
    }

    suspend fun logout() {
        sessionManager.logout()
    }
}