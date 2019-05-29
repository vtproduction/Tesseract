package com.midsummer.tesseract.core

import com.midsummer.tesseract.common.Config
import com.midsummer.tesseract.w3jl.entity.EntityWallet
import io.reactivex.Single

/**
 * Created by cityme on 27,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
interface CoreFunctions {

    fun createAccountFromMnemonics(mnemonics: String?, hdPath: String = Config.HDPath.TOMO) : Single<Pair<String?, Throwable?>>

    fun createAccountFromPrivateKey(privateKey: String?) : Single<Pair<String?, Throwable?>>

    fun createAccountFromAddress(address: String?) : Single<Pair<String?, Throwable?>>

    fun getActiveAccount() : Single<Pair<EntityWallet?, Throwable?>>

    fun getAllAccount() : Single<Pair<MutableList<EntityWallet>?, Throwable?>>

    fun getAccountByAddress(address: String?) : Single<Pair<EntityWallet?, Throwable?>>

    fun removeAccount(address: String?) : Single<Pair<EntityWallet?, Throwable?>>

    fun setAccountAsActive(address: String?) : Single<Pair<EntityWallet?, Throwable?>>

    fun setAccountName(address: String?, name: String) : Single<Pair<EntityWallet?, Throwable?>>
}