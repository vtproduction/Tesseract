package com.midsummer.tesseract.w3jl.components.signer

import android.util.Log
import com.midsummer.tesseract.common.LogTag.TAG_W3JL
import com.midsummer.tesseract.common.exception.InvalidPrivateKeyException
import com.midsummer.tesseract.habak.EncryptedModel
import com.midsummer.tesseract.habak.cryptography.Habak
import com.midsummer.tesseract.room.entity.account.AccountDAO
import com.midsummer.tesseract.w3jl.utils.WalletUtil
import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.entity.EntityWalletKey
import io.reactivex.Single
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
class SignerServiceImpl(var accountDA0: AccountDAO?, var habak: Habak?, var web3j: Web3j?) : SignerService {


    override fun signRawMessage( message: String?): Single<SignResult>? {

        return accountDA0?.getActiveAccount()
            ?.flatMap {activeAccount ->
                Single.create<SignResult> {
                    try{
                        val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData))
                        val account = EntityWalletKey.readFromString(wallet)

                        if (!WalletUtil.isValidPrivateKey(account?.privateKey)){
                            it.onError(InvalidPrivateKeyException())
                            return@create
                        }
                        if (account?.privateKey == null || !WalletUtil.isValidPrivateKey(account?.privateKey)){
                            it.onSuccess(SignResult(
                                SignStatus.SIGN_INVALID_CREDENTIAL,
                                message,
                                null,
                                null,
                                InvalidPrivateKeyException()
                            ))
                            return@create
                        }

                        if (message == null){
                            it.onSuccess(
                                SignResult(
                                    SignStatus.SIGN_INVALID_INPUT,
                                    null,
                                    null,
                                    account?.address,
                                    Exception("Null or empty input")
                                )
                            )
                            return@create
                        }

                        val credential = Credentials.create(account!!.privateKey)
                        val signature = Sign.signPrefixedMessage(message!!.toByteArray(Charset.defaultCharset()), credential.ecKeyPair)
                        val signed = Numeric.toHexString(signature.r) +
                                Numeric.cleanHexPrefix(Numeric.toHexString(signature.s)) +
                                Integer.toHexString(signature.v.toInt())
                        it.onSuccess(SignResult(
                            SignStatus.SIGN_SUCCESS,
                            message,
                            signed,
                            credential.address,
                            null
                        ))


                    }catch(t: Throwable){
                        Log.e(TAG_W3JL,"SignerServiceImpl > signRawMessage: ${t.localizedMessage}")
                        it.onSuccess(
                            SignResult(
                                SignStatus.SIGN_FAIL_GENERAL,
                                message,
                                null,
                                null,
                                t
                            )
                        )
                    }

                }
            }
    }

    override fun signMessage(message: String?): Single<SignResult>? {
        return signRawMessage( message)
    }

    override fun signPersonalMessage( message: String?): Single<SignResult>? {

        return accountDA0?.getActiveAccount()
            ?.flatMap {activeAccount ->
                Single.create<SignResult> {
                    try{
                        val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData))
                        val account = EntityWalletKey.readFromString(wallet)

                        if (!WalletUtil.isValidPrivateKey(account?.privateKey)){
                            it.onError(InvalidPrivateKeyException())
                            return@create
                        }
                        if (account?.privateKey == null || !WalletUtil.isValidPrivateKey(account?.privateKey)){
                            it.onSuccess(
                                SignResult(
                                    SignStatus.SIGN_INVALID_CREDENTIAL,
                                    message,
                                    null,
                                    null,
                                    InvalidPrivateKeyException()
                                )
                            )
                            return@create
                        }

                        if (message == null){
                            it.onSuccess(
                                SignResult(
                                    SignStatus.SIGN_INVALID_INPUT,
                                    null,
                                    null,
                                    account?.address,
                                    Exception("Null or empty input")
                                )
                            )
                            return@create
                        }

                        val credential = Credentials.create(account!!.privateKey)
                        val signature = Sign.signPrefixedMessage(Numeric.hexStringToByteArray(message), credential.ecKeyPair)
                        val signed = Numeric.toHexString(signature.r) +
                                Numeric.cleanHexPrefix(Numeric.toHexString(signature.s)) +
                                Integer.toHexString(signature.v.toInt())
                        it.onSuccess(
                            SignResult(
                                SignStatus.SIGN_SUCCESS,
                                message,
                                signed,
                                credential.address,
                                null
                            )
                        )
                        return@create

                    }catch(t: Throwable){
                        Log.e(TAG_W3JL,"SignerServiceImpl > signPersonalMessage: ${t.localizedMessage}")
                        it.onSuccess(SignResult(
                            SignStatus.SIGN_FAIL_GENERAL,
                            message,
                            null,
                            null,
                            t
                        ))
                        return@create
                    }

                }
            }
    }

    override fun signTransaction(
        recipient: String,
        amount: BigInteger?,
        gasPrice: BigInteger?,
        gasLimit: BigInteger?,
        payload: String?
    ): Single<SignResult>? {

        return accountDA0?.getActiveAccount()
            ?.flatMap {activeAccount ->
                Single.create<SignResult> {
                    try{
                        val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData))
                        val account = EntityWalletKey.readFromString(wallet)

                        if (!WalletUtil.isValidPrivateKey(account?.privateKey)){
                            it.onError(InvalidPrivateKeyException())
                            return@create
                        }

                        if (account?.privateKey == null || !WalletUtil.isValidPrivateKey(account.privateKey)){
                            it.onSuccess(SignResult(
                                SignStatus.SIGN_INVALID_CREDENTIAL,
                                null,
                                null,
                                null,
                                InvalidPrivateKeyException()
                            ))
                            return@create
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

                        it.onSuccess(
                            SignResult(
                                SignStatus.SIGN_SUCCESS,
                                "",
                                signedMessageHex,
                                credential.address,
                                null
                            )
                        )
                        return@create
                    }catch(t: Throwable){
                        Log.e(TAG_W3JL,"SignerServiceImpl > signPersonalMessage: ${t.localizedMessage}")
                        it.onSuccess(
                            SignResult(
                                SignStatus.SIGN_FAIL_GENERAL,
                                null,
                                null,
                                null,
                                t
                            )
                        )
                        return@create
                    }
                }
            }
    }
}