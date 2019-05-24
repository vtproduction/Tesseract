package com.midsummer.tesseract.core

import android.provider.Settings
import com.midsummer.tesseract.w3jl.constant.chain.Chain
import com.midsummer.tesseract.w3jl.constant.chain.CommonChain

/**
 * Created by cityme on 24,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class DefaultTesseractConfig : TesseractConfig() {

    override fun chain(): Chain {
        return CommonChain.TOMO_CHAIN
    }

    override fun habakAlias(): String {
        return "tesseract"
    }

    override fun roomHelperSalt(): String {
        return Settings.Secure.ANDROID_ID
    }
}