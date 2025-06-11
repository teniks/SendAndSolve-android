package su.sendandsolve.core.database.room

import android.content.Context
import android.os.Build
import android.os.FileObserver
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import su.sendandsolve.core.utils.SecurityFileObserver
import java.io.File
import java.security.InvalidKeyException
import java.security.KeyStore
import java.security.SecureRandom
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class DatabasePassphrase(private val context: Context) {
    private val keyStoreAlias = "sendandsolve_db_key"
    private val fileName = "passphrase.bin"

    init {
        if (Build.VERSION.SDK_INT >= 29) {
            val observer = SecurityFileObserver(
                context,
                File(context.filesDir, fileName),
                setOf(
                    FileObserver.DELETE
                )
            ) {
                cleanPassphrase() }
            observer.startWatching()
        }
    }

    fun getPassphrase(): ByteArray {
        val file = File(context.filesDir, fileName)
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

        val keyExists = keyStore.containsAlias(keyStoreAlias)

        val key = if (!keyExists) generateKey() else retrieveKey()

        return if (file.exists()) {
            decryptPassphrase(file, key)
        } else {
            val newPassphrase = generatePassphrase()
            encryptPassphrase(file, key, newPassphrase)
            newPassphrase
        }
    }

    fun cleanPassphrase() {
        KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
            deleteEntry(keyStoreAlias)
        }
        context.deleteFile(fileName)
    }

    private fun generateKey(): SecretKey {
        try{
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

            val spec = KeyGenParameterSpec.Builder(
                keyStoreAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .apply {
                    if (Build.VERSION.SDK_INT >= 28) {
                        setIsStrongBoxBacked(true)  // Для устройств с Secure Element
                    }
                }.build()

            keyGenerator.init(spec)
            return keyGenerator.generateKey()
        }
        catch (ex: Exception)  {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

            val spec = KeyGenParameterSpec.Builder(
                keyStoreAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
            keyGenerator.init(spec)
            return keyGenerator.generateKey()
        }
    }

    private fun generatePassphrase(): ByteArray {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES)
        keyGenerator.init(256, SecureRandom.getInstanceStrong())
        return keyGenerator.generateKey().encoded
    }

    private fun retrieveKey(): SecretKey {
        try {
            val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
            return keyStore.getKey(keyStoreAlias, null) as SecretKey
        } catch (ex: InvalidKeyException) {
            Log.e("DatabasePassphrase", "Error of retrieve key. The key will be cleaned.  \n${ex.message}")
            cleanPassphrase()
            return generateKey()
        }
    }

    private fun encryptPassphrase(file: File, key: SecretKey, passphrase: ByteArray) {
        // AES/GCM. GCM (Galois/Counter Mode) - это безопасный режим шифрования с аутентификацией.
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedPassphrase = cipher.doFinal(passphrase)
        val iv = cipher.iv

        try {
            context.openFileOutput(file.name, Context.MODE_PRIVATE).use {
                it.write(iv)
                it.write(encryptedPassphrase)
            }
        } finally {
            Arrays.fill(iv, 0.toByte())
            Arrays.fill(encryptedPassphrase, 0.toByte())
        }
    }

    private fun decryptPassphrase(file: File, key: SecretKey): ByteArray {
        // AES/GCM. GCM (Galois/Counter Mode) - это безопасный режим шифрования с аутентификацией.
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        context.openFileInput(file.name).use { stream ->
            val iv = ByteArray(12)
            stream.read(iv)
            val encrypted = stream.readBytes()
            try {
                cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
                return cipher.doFinal(encrypted)
            } finally {
                Arrays.fill(iv, 0.toByte())
                Arrays.fill(encrypted, 0.toByte())
            }
        }
    }
}