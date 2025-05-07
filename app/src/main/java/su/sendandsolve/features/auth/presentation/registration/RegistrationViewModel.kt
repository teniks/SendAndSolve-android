package su.sendandsolve.features.auth.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import su.sendandsolve.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.LoginChanged -> {
                _state.update { it.copy(login = event.login) }
            }
            is RegistrationEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password) }
            }
            is RegistrationEvent.NicknameChanged -> {
                _state.update { it.copy(nickname = event.nickname) }
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
}