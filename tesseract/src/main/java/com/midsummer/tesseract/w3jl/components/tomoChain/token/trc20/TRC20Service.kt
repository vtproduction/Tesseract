package com.midsummer.tesseract.w3jl.components.tomoChain.token.trc20

import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.listener.TransactionListener
import io.reactivex.Single
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Created by cityme on 23,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
interface TRC20Service {
    fun getBalance(address : String, tokenAddress: String) : Single<BigDecimal>
    fun getName(address : String, tokenAddress: String) : Single<String>
    fun getSymbol(address : String, tokenAddress: String) : Single<String>
    fun getDecimal(address : String, tokenAddress: String) : Single<Int>
    fun transferToken(
        tokenAddress: String,
        recipient: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ) : Single<String>
    fun transferToken(
        tokenAddress: String,
        recipient: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        callback: TransactionListener?
    )
    fun estimateTokenTransferGas(tokenAddress: String): Single<BigInteger>
    fun extractFunction(src: String) : TRC20FunctionType
    fun decodeHexData(src: String) : List<String>
}