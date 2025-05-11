package su.sendandsolve.features.auth.presentation.registration

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import su.sendandsolve.R
import su.sendandsolve.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _stateIsValid = MutableStateFlow(false)
    val stateIsValid: StateFlow<Boolean> = _stateIsValid
    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state

    init {
        viewModelScope.launch {
            state
                .drop(1)
                .collect {
                _stateIsValid.value = it.loginError == null &&
                        it.login.isNotBlank() &&
                        it.passwordError == null &&
                        it.password.isNotBlank() &&
                        it.confirmPasswordError == null &&
                        it.confirmPassword.isNotBlank() &&
                        it.nicknameError == null &&
                        it.nickname.isNotBlank() &&
                        it.error == null
            }
        }
    }

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.LoginChanged -> {
                _state.update { it.copy(login = event.login.trim(), loginError = validateLogin(event.login.trim())) }
            }
            is RegistrationEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password.trim(), passwordError = validatePassword(event.password.trim())) }
            }
            is RegistrationEvent.ConfirmPasswordChanged -> {
                _state.update { it.copy(confirmPassword = event.confirmPassword.trim(), confirmPasswordError = confirmPassword(event.confirmPassword.trim())) }
            }
            is RegistrationEvent.NicknameChanged -> {
                _state.update { it.copy(nickname = event.nickname.trim(), nicknameError = validateNickname(event.nickname.trim())) }
            }
            is RegistrationEvent.Submit -> register()
        }
    }

    private fun register(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = authRepository.register(
                _state.value.login,
                _state.value.password,
                _state.value.nickname
            )
            _state.update {
                it.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message,
                    isSuccess = result.isSuccess
                )
            }
        }
    }

    private fun validateLogin(login: String): String? {
        return when {
            login.length < 5 -> context.getString(R.string.login_is_not_long_enough)
            login.length > 256 -> context.getString(R.string.login_is_to_long)
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.length < 8 -> context.getString(R.string.password_is_not_long_enough)
            password.length > 128 -> context.getString(R.string.password_is_to_long)
            else -> null
        }
    }

    private fun confirmPassword(confirmPassword: String): String? {
        return when {
            confirmPassword != _state.value.password -> context.getString(R.string.passwords_dont_match)
            else -> null
        }
    }

    private fun validateNickname(nickname: String): String? {
        return when {
            nickname.length < 2 -> context.getString(R.string.nickname_is_not_long_enough)
            nickname.length > 256 -> context.getString(R.string.nickname_is_to_long)
            else -> null
        }
    }
}