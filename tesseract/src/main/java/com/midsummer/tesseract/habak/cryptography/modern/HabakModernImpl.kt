package com.midsummer.tesseract.habak.cryptography.modern

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.midsummer.tesseract.habak.Constant
import com.midsummer.tesseract.habak.EncryptedModel
import com.midsummer.tesseract.habak.cryptography.Habak
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Created by NienLe on 2019-04-28,April,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class HabakModernImpl(var alias : String) : Habak {


    private var keyStore: KeyStore? = null

    /**
     * generate a keystore secret key that use to encrypt/decrypt data
     * This key is store in hardware layer and only can access within app at runtime
     * Specify the key by alias
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun generateSecretKey() : SecretKey {
        val keyGenerator = KeyGenerator
            .getInstance(KeyProperties.KEY_ALGORITHM_AES, Constant.ANDROID_KEY_STORE)

        keyGenerator.init(
            KeyGenParameterSpec.Builder(alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build())
        return keyGenerator.generateKey()
    }


    /**
     * Get secret key from hardware along with 'alias' params
     * return the secret key
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun getSecretKey() : SecretKey {
        return (keyStore?.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
    }


    /**
     * Init the Keystore system with default params.
     * Check if the key with alias 'alias' is already existed, generate if need
     */
    override fun initialize()  : Habak{
        keyStore = KeyStore.getInstance(Constant.ANDROID_KEY_STORE)
        keyStore?.load(null)
        if (!keyStore?.containsAlias(alias)!!)
            generateSecretKey()
        return this
    }

    /**
     * Encrypt the plain text
     * @param plainText
     * @return EncryptedModel object contain the encrypted data, the IV
     * and the current timeStamp
     */
    override fun encrypt(plainText: String): EncryptedModel {
        return try {
            val cipher = Cipher.getInstance(Constant.AES_MODE_FROM_M)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            val iv = cipher.iv
            val encrypted = cipher.doFinal(plainText.toByteArray(charset(Constant.UTF8)))
            val now = Calendar.getInstance().timeInMillis
            return EncryptedModel(encrypted, iv, now)
        } catch (e: Exception) {
            EncryptedModel(ByteArray(0), ByteArray(0), 0)
        }
    }

    /**
     * Decrypt data
     * @param data the EncryptedModel object to decrypt
     * @return decrypted plain string
     * and the current timeStamp
     */
    override fun decrypt(data: EncryptedModel): String {
        return try {
            val cipher = Cipher.getInstance(Constant.AES_MODE_FROM_M)
            val spec = GCMParameterSpec(128, data.iv)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
            return String(cipher.doFinal(data.data), Charsets.UTF_8)
        } catch (e: Exception) {
            ""
        }
    }

}