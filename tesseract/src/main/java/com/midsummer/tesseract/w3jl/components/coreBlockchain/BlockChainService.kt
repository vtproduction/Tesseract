package com.midsummer.tesseract.w3jl.components.coreBlockchain

import io.reactivex.Single
import java.math.BigInteger

interface BlockChainService {
    fun getAccountBalance(lastBalance: BigInteger?): Single<BigInteger>
    fun getTransactionCount(): Single<BigInteger>
    fun transfer(
        recipient: String,
        amount: BigInteger?,
        gasPrice: BigInteger?,
        gasLimit: BigInteger?,
        payload: String?
    ): Single<String>
}
