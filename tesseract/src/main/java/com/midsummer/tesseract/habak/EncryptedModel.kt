package com.midsummer.tesseract.habak

import android.util.Base64
import com.google.gson.Gson
import java.nio.charset.Charset

/**
 * Created by NienLe on 4/23/19,April,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class EncryptedModel(var data: ByteArray? = null,
                     var iv: ByteArray? = null,
                     var lastUpdate: Long? = null) {


    override fun toString(): String {
        return "EncryptedModel(data='${Base64.encodeToString(data, Base64.DEFAULT)}', iv='${Base64.encodeToString(iv, Base64.DEFAULT)}', lastUpdate=$lastUpdate)"
    }

    fun toByteArrayString(): String {
        return "EncryptedModel(data='${data?.contentToString()}', iv='${iv?.contentToString()}', lastUpdate=$lastUpdate)"
    }

    fun writeToString() : String{
        val s = Gson().toJson(this)
        val data = s.toByteArray(Charset.defaultCharset())
        return Base64.encodeToString(data, Base64.DEFAULT)
    }

    companion object {
        fun readFromString(src : String) : EncryptedModel {
            val data = Base64.decode(src, Base64.DEFAULT)
            val s = String(data, Charset.defaultCharset())
            return Gson().fromJson(s, EncryptedModel::class.java)
        }
    }

}
