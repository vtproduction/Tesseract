package com.midsummer.tesseract.w3jl.components.coreBlockchain

import com.midsummer.tesseract.w3jl.components.wallet.EntityWallet
import io.reactivex.Single
import java.math.BigInteger

/**
 * Created by cityme on 22,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
interface CoreBlockchainService {

    fun getAccountBalance(account: EntityWallet?, lastBalance: BigInteger?) : Single<BigInteger>
    fun getTransactionCount(account: EntityWallet?) : Single<BigInteger>
    fun transfer(account: EntityWallet?,
                        recipient: String,
                        amount: BigInteger?,
                        gasPrice: BigInteger?,
                        gasLimit: BigInteger?,
                        payload: String?) : Single<String>
}