package su.sendandsolve.features.auth.presentation.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import su.sendandsolve.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _state = MutableStateFlow(AuthorizationState())
    val state: MutableStateFlow<AuthorizationState> = _state

    fun onEvent(event: AuthorizationEvent) {
        when (event) {
            is AuthorizationEvent.LoginChanged -> {
                _state.update { it.copy(login = event.login) }
            }
            is AuthorizationEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password) }
            }
            is AuthorizationEvent.Submit -> authorize()
        }
    }

    private fun authorize(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = authRepository.login(
                _state.value.login,
                _state.value.password
            )
            _state.update {
                it.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message,
                    isSuccess = result.isFailure
                )
            }
        }
    }
}