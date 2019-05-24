package com.midsummer.tesseract.w3jl.utils

import org.web3j.crypto.WalletUtils
import java.security.PrivateKey

/**
 * Created by NienLe on 2019-05-03,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
object ValidationUtil {

    fun isValidAddress(address: String?) : Boolean{
        return WalletUtils.isValidAddress(address)
    }

    fun isValidPrivateKey(privateKey: String?) : Boolean{
        return WalletUtils.isValidPrivateKey(privateKey)
    }
}