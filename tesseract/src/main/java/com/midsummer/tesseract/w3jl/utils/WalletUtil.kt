package com.midsummer.tesseract.w3jl.utils

import android.util.Log
import com.midsummer.tesseract.common.LogTag
import org.web3j.crypto.MnemonicUtils
import org.web3j.crypto.WalletUtils
import java.security.PrivateKey
import java.security.SecureRandom

/**
 * Created by NienLe on 2019-05-03,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
object WalletUtil {

    fun getWordList(): List<String> {
        return try{
            MnemonicUtils.getWords()
        }catch(e: Exception){
            Log.e(LogTag.TAG_W3JL, "getWordList",e)
            arrayListOf()
        }
    }


    fun isValidAddress(address: String?) : Boolean{
        return WalletUtils.isValidAddress(address)
    }

    fun isValidPrivateKey(privateKey: String?) : Boolean{
        return WalletUtils.isValidPrivateKey(privateKey)
    }

    fun isValidMnemonics(mnemonics: String?, matchCurrentWordList: Boolean) : Boolean{
        return try{
            val list = mnemonics?.toLowerCase()?.split(" ")

            if (list?.size != 12)  false

            if (matchCurrentWordList){

            }


            true
        }catch(t: Throwable){
            Log.e(LogTag.TAG_W3JL, "isValidMnemonics",t)
            false
        }
    }

    fun generateMnemonics(): String {
        val initialEntropy = ByteArray(16)
        val secureRandom =  SecureRandom()
        secureRandom.nextBytes(initialEntropy)

        return MnemonicUtils.generateMnemonic(initialEntropy)
    }
}