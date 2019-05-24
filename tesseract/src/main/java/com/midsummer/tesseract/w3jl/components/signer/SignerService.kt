package com.midsummer.tesseract.w3jl.components.signer

import com.midsummer.tesseract.w3jl.entity.EntityWallet
import java.math.BigInteger

/**
 * Created by cityme on 22,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
interface SignerService {

    fun signRawMessage( message: String?) : SignResult
    fun signMessage( message: String?) : SignResult
    fun signPersonalMessage( message: String?) : SignResult

    fun signTransaction(
                        recipient: String,
                        amount: BigInteger?,
                        gasPrice: BigInteger?,
                        gasLimit: BigInteger?,
                        payload: String?) : SignResult
}