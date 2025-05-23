package su.sendandsolve.features.auth.presentation

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import su.sendandsolve.R
import su.sendandsolve.core.utils.CurrentUser
import su.sendandsolve.features.tasks.domain.Repository
import su.sendandsolve.features.tasks.domain.model.User
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuthCheckViewModel @Inject constructor(
    private val currentUser: CurrentUser,
    private val userRepository: Repository<User>,
    @ApplicationContext private val context: Context
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

                    val user = userRepository.getById(userId) ?: throw NotFoundException(context.getString(R.string.user_was_not_found))

                    _state.value = AuthState.Authenticated(userId)
                } else {
                    currentUser.logout()
                    _state.value = AuthState.Unauthenticated
                }
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "Unknown error")
            } catch (e: NotFoundException) {
                Log.e("SessionManager", "No user with this id was found", e)
                _state.value = AuthState.Error(e.message ?: "The user was not found")
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