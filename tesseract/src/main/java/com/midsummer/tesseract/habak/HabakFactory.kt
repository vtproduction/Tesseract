package com.midsummer.tesseract.habak

import android.content.Context
import android.os.Build
import com.midsummer.tesseract.habak.cryptography.Habak
import com.midsummer.tesseract.habak.cryptography.legacy.HabakLegacyImpl
import com.midsummer.tesseract.habak.cryptography.modern.HabakModernImpl
import com.midsummer.tesseract.habak.cryptography.modern.HabakModernPasswordImpl

/**
 * Created by NienLe on 2019-04-28,April,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class HabakFactory {

    var alias : String? = null
    var password : String = ""



    fun withAlias(alias : String?) : HabakFactory {
        this.alias = alias
        return this
    }


    fun withPassword(password : String?) : HabakFactory {
        this.password = password ?: ""
        return this
    }



    fun build() : Habak {
        requireNotNull(alias, lazyMessage = { "alias must not be null!" })
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (password.isBlank()) {
                HabakModernImpl(alias!!).initialize()
            } else {
                HabakModernPasswordImpl(alias!!, password).initialize()
            }
        }
        else{
            HabakLegacyImpl(alias!!).initialize()
        }
    }
}