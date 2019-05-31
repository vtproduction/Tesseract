package com.midsummer.tesseract.w3jl.components.coreBlockchain

import android.util.Log
import com.midsummer.tesseract.common.LogTag
import com.midsummer.tesseract.common.exception.CorruptedHabakException
import com.midsummer.tesseract.common.exception.DefaultAccountNotFoundException
import com.midsummer.tesseract.common.exception.InvalidAddressException
import com.midsummer.tesseract.common.exception.InvalidPrivateKeyException
import com.midsummer.tesseract.habak.EncryptedModel
import com.midsummer.tesseract.habak.cryptography.Habak
import com.midsummer.tesseract.room.entity.account.AccountDAO
import com.midsummer.tesseract.room.entity.account.DatabaseAccount
import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.entity.EntityWalletKey
import com.midsummer.tesseract.w3jl.utils.WalletUtil
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
class BlockChainServiceImpl(var accountDA0: AccountDAO?, var habak: Habak?, var web3j: Web3j?) : BlockChainService {

    override fun getAccountBalance(lastBalance: BigInteger?): Single<BigInteger> {
        return Single.create{ emitter ->
            try {
                val account = accountDA0?.getActiveAccount()?.blockingGet()
                if(account == null || !WalletUtils.isValidAddress(account?.address)){
                    emitter.onError(InvalidAddressException())
                    return@create
                }else{
                    web3j?.ethGetBalance(account?.address, DefaultBlockParameterName.LATEST)
                        ?.flowable()
                        ?.doOnError {
                            emitter.onSuccess(lastBalance ?: BigInteger.ZERO)
                        }
                        ?.subscribe ({e -> emitter.onSuccess(e.balance)} , { _ -> emitter.onSuccess(lastBalance ?: BigInteger.ZERO)})
                }
            }catch (e : Exception){
                //emitter.tryOnError(e)
                Log.e(LogTag.TAG_W3JL,"CoreBlockChainServiceImpl > getAccountBalance: ${e.localizedMessage}")
                emitter.onSuccess(lastBalance ?: BigInteger.ZERO)
            }
        }
    }

    override fun getTransactionCount(): Single<BigInteger> {
        return Single.create{ emitter ->
            try {
                val account = accountDA0?.getActiveAccount()?.blockingGet()
                if(account == null || !WalletUtils.isValidAddress(account?.address)){
                    emitter.onError(InvalidAddressException())
                    return@create
                }else{
                    web3j?.ethGetTransactionCount(account?.address, DefaultBlockParameterName.LATEST)
                        ?.flowable()
                        ?.doOnError {
                            emitter.onSuccess(BigInteger.ZERO)
                        }
                        ?.subscribe ({e -> emitter.onSuccess(e.transactionCount)} , { _ -> emitter.onSuccess(BigInteger.ZERO)})
                }
            }catch (e : Exception){
                //emitter.tryOnError(e)
                Log.e(LogTag.TAG_W3JL,"CoreBlockChainServiceImpl > getTransactionCount: ${e.localizedMessage}")
                emitter.onSuccess(BigInteger.ZERO)
            }
        }
    }

    override fun getAccountBalance(address: String?, lastBalance: BigInteger?): Single<BigInteger> {
        return Single.create{ emitter ->
            try {

                if(address == null || !WalletUtils.isValidAddress(address)){
                    emitter.onError(InvalidAddressException())
                    return@create
                }else{
                    web3j?.ethGetBalance(address, DefaultBlockParameterName.LATEST)
                        ?.flowable()
                        ?.doOnError {
                            emitter.onSuccess(lastBalance ?: BigInteger.ZERO)
                        }
                        ?.subscribe ({e -> emitter.onSuccess(e.balance)} , { _ -> emitter.onSuccess(lastBalance ?: BigInteger.ZERO)})
                }
            }catch (e : Exception){
                //emitter.tryOnError(e)
                Log.e(LogTag.TAG_W3JL,"CoreBlockChainServiceImpl > getAccountBalance: ${e.localizedMessage}")
                emitter.onSuccess(lastBalance ?: BigInteger.ZERO)
            }
        }
    }

    override fun getTransactionCount(address: String?): Single<BigInteger> {
        return Single.create{ emitter ->
            try {

                if(address == null || !WalletUtils.isValidAddress(address)){
                    emitter.onError(InvalidAddressException())
                    return@create
                }else{
                    web3j?.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
                        ?.flowable()
                        ?.doOnError {
                            emitter.onSuccess(BigInteger.ZERO)
                        }
                        ?.subscribe ({e -> emitter.onSuccess(e.transactionCount)} , { _ -> emitter.onSuccess(BigInteger.ZERO)})
                }
            }catch (e : Exception){
                //emitter.tryOnError(e)
                Log.e(LogTag.TAG_W3JL,"CoreBlockChainServiceImpl > getTransactionCount: ${e.localizedMessage}")
                emitter.onSuccess(BigInteger.ZERO)
            }
        }
    }

    override fun transfer(
        recipient: String,
        amount: BigInteger?,
        gasPrice: BigInteger?,
        gasLimit: BigInteger?,
        payload: String?
    ): Single<String> {
        return Single.create { emitter ->
            try{
                val activeAccount = accountDA0?.getActiveAccount()?.blockingGet()
                if (activeAccount == null){
                    emitter.onError(DefaultAccountNotFoundException())
                    return@create
                }
                val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData))
                val account = EntityWalletKey.readFromString(wallet)

                if (!WalletUtil.isValidPrivateKey(account?.privateKey)){
                    emitter.onError(InvalidPrivateKeyException())
                    return@create
                }

                val credentials = Credentials.create(account?.privateKey)
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
                Log.e(LogTag.TAG_W3JL,"CoreBlockChainServiceImpl > transfer: ${t.localizedMessage}")
                emitter.onSuccess("")
            }
        }
    }
}