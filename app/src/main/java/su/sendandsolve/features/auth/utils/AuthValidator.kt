package su.sendandsolve.features.auth.utils

object AuthValidator {
    fun isValidPasswordLength(password: String): Boolean {
        return password.length >= 8
    }
}