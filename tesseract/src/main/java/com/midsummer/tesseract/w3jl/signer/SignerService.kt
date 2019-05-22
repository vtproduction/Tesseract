package com.midsummer.tesseract.w3jl.signer

import com.midsummer.tesseract.w3jl.wallet.EntityWallet
import java.math.BigInteger

/**
 * Created by cityme on 22,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
interface SignerService {

    fun signRawMessage(account: EntityWallet?, message: String?) : SignResult
    fun signMessage(account: EntityWallet?, message: String?) :  SignResult
    fun signPersonalMessage(account: EntityWallet?, message: String?) :  SignResult

    fun signTransaction(account: EntityWallet?,
                        recipient: String,
                        amount: BigInteger?,
                        gasPrice: BigInteger?,
                        gasLimit: BigInteger?,
                        payload: String?) : SignResult
}