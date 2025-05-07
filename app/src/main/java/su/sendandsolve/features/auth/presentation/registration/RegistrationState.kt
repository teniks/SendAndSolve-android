package su.sendandsolve.features.auth.presentation.registration

data class RegistrationState(
    val login: String = "",
    val password: String = "",
    val nickname: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

sealed class RegistrationEvent {
    data class LoginChanged(val login: String) : RegistrationEvent()
    data class PasswordChanged(val password: String) : RegistrationEvent()
    data class NicknameChanged(val nickname: String) : RegistrationEvent()
    data object Submit : RegistrationEvent()
}