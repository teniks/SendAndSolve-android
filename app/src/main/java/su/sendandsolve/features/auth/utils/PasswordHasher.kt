package su.sendandsolve.features.auth.utils

import java.security.MessageDigest
import java.util.Base64

object PasswordHasher {

    fun generateSalt(): String {
        val random = ByteArray(32)
        java.security.SecureRandom().nextBytes(random)
        return Base64.getEncoder().encodeToString(random)
    }

    fun hash(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val saltedPassword = salt + password
        val hashBytes = digest.digest(saltedPassword.toByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }

    fun verify(password: String, salt: String, storeHash: String): Boolean {
        return hash(password, salt) == storeHash
    }
}