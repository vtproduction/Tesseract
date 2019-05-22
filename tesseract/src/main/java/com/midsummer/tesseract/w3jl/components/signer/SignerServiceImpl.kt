package com.midsummer.tesseract.w3jl.components.signer

import android.util.Log
import com.midsummer.tesseract.common.LogTag.TAG_W3JL
import com.midsummer.tesseract.common.exception.InvalidPrivateKeyException
import com.midsummer.tesseract.w3jl.utils.ValidationUtil
import com.midsummer.tesseract.w3jl.components.wallet.EntityWallet
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.Sign
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.nio.charset.Charset

/**
 * Created by cityme on 22,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class SignerServiceImpl(var web3j: Web3j?) : SignerService {


    override fun signRawMessage(account: EntityWallet?, message: String?): SignResult {
        return try{
            if (account?.privateKey == null || !ValidationUtil.isValidPrivateKey(account.privateKey)){
                SignResult(
                    SignStatus.SIGN_INVALID_CREDENTIAL,
                    message,
                    null,
                    null,
                    InvalidPrivateKeyException()
                )
            }

            if (message == null){
                SignResult(
                    SignStatus.SIGN_INVALID_INPUT,
                    null,
                    null,
                    account?.address,
                    Exception("Null or empty input")
                )
            }

            val credential = Credentials.create(account!!.privateKey)
            val signature = Sign.signPrefixedMessage(message!!.toByteArray(Charset.defaultCharset()), credential.ecKeyPair)
            val signed = Numeric.toHexString(signature.r) +
                    Numeric.cleanHexPrefix(Numeric.toHexString(signature.s)) +
                    Integer.toHexString(signature.v.toInt())
            SignResult(
                SignStatus.SIGN_SUCCESS,
                message,
                signed,
                credential.address,
                null
            )


        }catch(t: Throwable){
            Log.e(TAG_W3JL,"SignerServiceImpl > signRawMessage: ${t.localizedMessage}")
            SignResult(
                SignStatus.SIGN_FAIL_GENERAL,
                message,
                null,
                null,
                t
            )
        }
    }

    override fun signMessage(account: EntityWallet?, message: String?): SignResult {
        return signRawMessage(account, message)
    }

    override fun signPersonalMessage(account: EntityWallet?, message: String?): SignResult {
        return try{
            if (account?.privateKey == null || !ValidationUtil.isValidPrivateKey(account.privateKey)){
                SignResult(
                    SignStatus.SIGN_INVALID_CREDENTIAL,
                    message,
                    null,
                    null,
                    InvalidPrivateKeyException()
                )
            }

            if (message == null){
                SignResult(
                    SignStatus.SIGN_INVALID_INPUT,
                    null,
                    null,
                    account?.address,
                    Exception("Null or empty input")
                )
            }

            val credential = Credentials.create(account!!.privateKey)
            val signature = Sign.signPrefixedMessage(Numeric.hexStringToByteArray(message), credential.ecKeyPair)
            val signed = Numeric.toHexString(signature.r) +
                    Numeric.cleanHexPrefix(Numeric.toHexString(signature.s)) +
                    Integer.toHexString(signature.v.toInt())
            SignResult(
                SignStatus.SIGN_SUCCESS,
                message,
                signed,
                credential.address,
                null
            )


        }catch(t: Throwable){
            Log.e(TAG_W3JL,"SignerServiceImpl > signPersonalMessage: ${t.localizedMessage}")
            SignResult(
                SignStatus.SIGN_FAIL_GENERAL,
                message,
                null,
                null,
                t
            )
        }
    }

    override fun signTransaction(
        account: EntityWallet?,
        recipient: String,
        amount: BigInteger?,
        gasPrice: BigInteger?,
        gasLimit: BigInteger?,
        payload: String?
    ): SignResult {
        return try{
            if (account?.privateKey == null || !ValidationUtil.isValidPrivateKey(account.privateKey)){
                SignResult(
                    SignStatus.SIGN_INVALID_CREDENTIAL,
                    null,
                    null,
                    null,
                    InvalidPrivateKeyException()
                )
            }

            val credential = Credentials.create(account?.privateKey)
            val realAmount = amount ?: BigInteger.ZERO
            val from = credential.address
            val ethGetTransactionCount = web3j?.ethGetTransactionCount(
                from, DefaultBlockParameterName.LATEST)?.sendAsync()?.get()
            val nonce = ethGetTransactionCount?.transactionCount
            val rawTransaction = if (payload == null || payload.isEmpty()){
                RawTransaction.createEtherTransaction(
                    nonce, gasPrice, gasLimit, recipient, realAmount)
            }
            else{
                RawTransaction.createTransaction(nonce, gasPrice, gasLimit, recipient, realAmount, payload)
            }
            val signedMessage = TransactionEncoder.signMessage(rawTransaction, credential)
            val signedMessageHex  = Numeric.toHexString(signedMessage)

            SignResult(
                SignStatus.SIGN_SUCCESS,
                "",
                signedMessageHex,
                credential.address,
                null
            )


        }catch(t: Throwable){
            Log.e(TAG_W3JL,"SignerServiceImpl > signPersonalMessage: ${t.localizedMessage}")
            SignResult(
                SignStatus.SIGN_FAIL_GENERAL,
                null,
                null,
                null,
                t
            )
        }
    }
}