package com.midsummer.tesseract.w3jl.components.coreBlockchain

import android.util.Log
import com.midsummer.tesseract.common.LogTag
import com.midsummer.tesseract.common.exception.InvalidAddressException
import com.midsummer.tesseract.common.exception.InvalidPrivateKeyException
import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.utils.ValidationUtil
import io.reactivex.Single
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Numeric
import java.math.BigInteger

/**
 * Created by cityme on 22,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class CoreBlockchainServiceImpl(var web3j: Web3j?) : CoreBlockchainService {

    override fun getAccountBalance(account: EntityWallet?, lastBalance: BigInteger?): Single<BigInteger> {
        return Single.create{ emitter ->
            try {

                if(account == null || !WalletUtils.isValidAddress(account.address)){
                    emitter.onError(InvalidAddressException())
                    return@create
                }else{
                    web3j?.ethGetBalance(account.address, DefaultBlockParameterName.LATEST)
                        ?.flowable()
                        ?.doOnError {
                            emitter.onSuccess(lastBalance ?: BigInteger.ZERO)
                        }
                        ?.subscribe ({e -> emitter.onSuccess(e.balance)} , { _ -> emitter.onSuccess(lastBalance ?: BigInteger.ZERO)})
                }
            }catch (e : Exception){
                //emitter.tryOnError(e)
                Log.e(LogTag.TAG_W3JL,"CoreBlockchainServiceImpl > getAccountBalance: ${e.localizedMessage}")
                emitter.onSuccess(lastBalance ?: BigInteger.ZERO)
            }
        }
    }

    override fun getTransactionCount(account: EntityWallet?): Single<BigInteger> {
        return Single.create{ emitter ->
            try {

                if(account == null || !WalletUtils.isValidAddress(account.address)){
                    emitter.onError(InvalidAddressException())
                    return@create
                }else{
                    web3j?.ethGetTransactionCount(account.address, DefaultBlockParameterName.LATEST)
                        ?.flowable()
                        ?.doOnError {
                            emitter.onSuccess(BigInteger.ZERO)
                        }
                        ?.subscribe ({e -> emitter.onSuccess(e.transactionCount)} , { _ -> emitter.onSuccess(BigInteger.ZERO)})
                }
            }catch (e : Exception){
                //emitter.tryOnError(e)
                Log.e(LogTag.TAG_W3JL,"CoreBlockchainServiceImpl > getTransactionCount: ${e.localizedMessage}")
                emitter.onSuccess(BigInteger.ZERO)
            }
        }
    }

    override fun transfer(
        account: EntityWallet?,
        recipient: String,
        amount: BigInteger?,
        gasPrice: BigInteger?,
        gasLimit: BigInteger?,
        payload: String?
    ): Single<String> {
        return Single.create { emitter ->
            try{
                if (account?.privateKey == null || !ValidationUtil.isValidPrivateKey(account.privateKey)){
                    emitter.onError(InvalidPrivateKeyException())
                    return@create
                }

                val credentials = Credentials.create(account.privateKey)
                val realAmount = amount ?: BigInteger.ZERO
                val from = credentials.address
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
                val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
                val signedMessageHex  = Numeric.toHexString(signedMessage)
                web3j?.ethSendRawTransaction(signedMessageHex)?.flowable()
                    ?.doOnError {

                    }
                    ?.subscribe ({e -> emitter.onSuccess(e.transactionHash)} , { _ -> emitter.onSuccess("")})

            }catch(t: Throwable){
                Log.e(LogTag.TAG_W3JL,"CoreBlockchainServiceImpl > transfer: ${t.localizedMessage}")
                emitter.onSuccess("")
            }
        }
    }
}