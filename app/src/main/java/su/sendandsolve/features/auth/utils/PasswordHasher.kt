package su.sendandsolve.features.auth.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHasher {

    private const val ITERATIONS = 65536
    private const val HASH_LENGTH = 256

    fun generateSalt(): String {
        val salt = generateSaltBytes()
        return Base64.getEncoder().encodeToString(salt)
    }

    private fun generateSaltBytes(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(64)
        random.nextBytes(salt)
        return salt
    }

    fun hash(password: String, salt: String): String {
        val saltBytes = Base64.getDecoder().decode(salt)
        val hash = hash(password, saltBytes)
        return Base64.getEncoder().encodeToString(hash)
    }

    private fun hash(password: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_LENGTH)

        try {
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
            return factory.generateSecret(spec).encoded
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

    fun verify(password: String, salt: String, storeHash: String): Boolean {
        val saltBytes = Base64.getDecoder().decode(salt)
        val storedHashBytes = Base64.getDecoder().decode(storeHash)
        val newHash = hash(password, saltBytes)
        return MessageDigest.isEqual(newHash, storedHashBytes)
    }
}