package com.midsummer.tesseract.habak.cryptography

import com.midsummer.tesseract.habak.EncryptedModel

/**
 * Created by NienLe on 2019-04-28,April,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
interface Habak{

    fun initialize() : Habak
    fun encrypt(plainText: String) : EncryptedModel
    fun decrypt(data: EncryptedModel) : String
}
