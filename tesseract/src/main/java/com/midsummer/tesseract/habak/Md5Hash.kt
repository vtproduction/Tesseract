package com.midsummer.tesseract.habak

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by NienLe on 4/23/19,April,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class Md5Hash {

    private val salt = "DGE$5SGr@3VsHYUMas2323E4d57vfBfFSTRU@!DSH(*%FDSdfg13sgfsg"


    fun hash(input: String?) : String {
        var md5 = ""
        if (null == input)
            return ""

        val message = input + salt
        try {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(message.toByteArray(), 0, message.length)
            md5 = BigInteger(1, digest.digest()).toString(16)

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return md5
    }


    fun hashAndCompare(input: String?, sample: String) : Boolean{
        return hash(input) == sample
    }

}
