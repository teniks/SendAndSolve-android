package su.sendandsolve.features.auth.presentation.authorization

data class AuthorizationState(
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

sealed class AuthorizationEvent {
    data class LoginChanged(val login: String) : AuthorizationEvent()
    data class PasswordChanged(val password: String) : AuthorizationEvent()
    data object Submit : AuthorizationEvent()
}
