package su.sendandsolve.features.auth.presentation

import android.content.res.Resources.NotFoundException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import su.sendandsolve.core.utils.CurrentUser
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.User
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuthCheckViewModel @Inject constructor(
    private val currentUser: CurrentUser,
    private val userRepository: Repository<User>
): ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Loading)
    val state: MutableStateFlow<AuthState> = _state

    init {
        checkAuth()
    }

    fun checkAuth() {
        viewModelScope.launch {
            try {
                val userId = currentUser.getUserId()
                if (userId != null) {

                    val user = userRepository.getById(userId) ?: throw NotFoundException("User with ID $userId not found")

                    _state.value = AuthState.Authenticated(userId)
                } else {
                    currentUser.logout()
                    _state.value = AuthState.Unauthenticated
                }
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "Unknown error")
            } catch (e: NotFoundException) {
                Log.e("SessionManager", "No user with this id was found", e)
                _state.value = AuthState.Error(e.message ?: "No user with this id was found")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            currentUser.logout()
            _state.value = AuthState.Unauthenticated
        }
    }

    sealed class AuthState {
        data object Loading : AuthState()
        data class Authenticated(val userId: UUID) : AuthState()
        data object Unauthenticated : AuthState()
        data class Error(val message: String) : AuthState()
    }
}