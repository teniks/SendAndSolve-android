package su.sendandsolve.features.auth.domain.repository

import su.sendandsolve.core.database.room.repository.UserRepository
import su.sendandsolve.core.utils.CurrentUser
import su.sendandsolve.features.auth.utils.PasswordHasher
import su.sendandsolve.features.tasks.domain.model.User
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val currentUser: CurrentUser
) {
    suspend fun register(login: String, password: String, nickname: String): Result<Unit> {
        return try {
            if (userRepository.getCount(login) > 0) {
                return Result.failure(Exception("User already exists"))
            }

            val userSalt = PasswordHasher.generateSalt()

            val user = User(
                uuid = UUID.randomUUID(),
                login = login,
                passwordHash = PasswordHasher.hash(password, userSalt),
                salt = userSalt,
                nickname = nickname,
                lastModified = Instant.now()
            )

            userRepository.insert(user)
            currentUser.setCurrentUser(user.uuid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(login: String, password: String): Result<Unit> {
        return try {
            val userByLogin = userRepository.getByLogin(login) ?:
                return Result.failure(Exception("User not found"))

            if (!PasswordHasher.verify(password, userByLogin.salt, userByLogin.passwordHash)) {
                return Result.failure(Exception("Invalid password"))
            }

            val user = userRepository.getById(userByLogin.uuid) ?:
                throw IllegalStateException("User inconsistency")

            currentUser.setCurrentUser(user.uuid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(){
        currentUser.logout()
    }
}