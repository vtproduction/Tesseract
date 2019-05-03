package com.midsummer.tesseract.habak.cryptography.legacy

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
class HabakLegacyImpl(var alias : String) : Habak {


    override fun initialize() : Habak {
        throw RuntimeException("Legacy version of Habak must be implemented")
    }

    override fun encrypt(plainText: String): EncryptedModel {
        throw RuntimeException("Legacy version of Habak must be implemented")
    }

    override fun decrypt(data: EncryptedModel): String {
        throw RuntimeException("Legacy version of Habak must be implemented")
    }
}