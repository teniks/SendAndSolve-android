package su.sendandsolve.features.auth.presentation.registration

data class RegistrationState(
    val login: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nickname: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val nicknameError: String? = null,
    val isSuccess: Boolean = false
)

sealed class RegistrationEvent {
    data class LoginChanged(val login: String) : RegistrationEvent()
    data class PasswordChanged(val password: String) : RegistrationEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegistrationEvent()
    data class NicknameChanged(val nickname: String) : RegistrationEvent()
    data object Submit : RegistrationEvent()
}