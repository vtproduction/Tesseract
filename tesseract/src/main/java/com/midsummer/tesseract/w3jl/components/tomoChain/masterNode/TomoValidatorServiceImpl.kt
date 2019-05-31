package com.midsummer.tesseract.w3jl.components.tomoChain.masterNode

import android.annotation.SuppressLint
import com.midsummer.tesseract.common.exception.InvalidPrivateKeyException
import com.midsummer.tesseract.habak.EncryptedModel
import com.midsummer.tesseract.habak.cryptography.Habak
import com.midsummer.tesseract.room.entity.account.AccountDAO
import com.midsummer.tesseract.room.entity.account.EntityAccount
import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.entity.EntityWalletKey
import com.midsummer.tesseract.w3jl.listener.TransactionListener
import com.midsummer.tesseract.w3jl.utils.WalletUtil
import io.reactivex.Single
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Numeric
import java.lang.Exception
import java.math.BigInteger
import java.util.*

/**
 * Created by cityme on 22,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class TomoValidatorServiceImpl(var accountDA0: AccountDAO?, var habak: Habak?, var web3j: Web3j?) : TomoValidatorService {

    private val CONTRACT_ADDRESS = "0x0000000000000000000000000000000000000088"

    override fun createVoteData(
        candidate: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ): Single<String>? {
        return accountDA0?.getActiveAccount()
            ?.flatMap {
                createVoteDataWithAccount(candidate, amount, gasPrice, gasLimit,it)
            }
    }

    private fun createVoteDataWithAccount(
        candidate: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        activeAccount: EntityAccount
    ): Single<String>{
        return Single.create{
            try {
                val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData))
                val account = EntityWalletKey.readFromString(wallet)

                if (!WalletUtil.isValidPrivateKey(account?.privateKey)){
                    it.onError(InvalidPrivateKeyException())
                    return@create
                }
                val credentials = Credentials.create(account?.privateKey)
                val from = credentials.address
                val ethGetTransactionCount = web3j?.ethGetTransactionCount(
                    from, DefaultBlockParameterName.LATEST)?.sendAsync()?.get()

                val nonce = ethGetTransactionCount?.transactionCount
                val function = Function(
                    "vote",
                    listOf(Address(candidate)),
                    Collections.emptyList())
                val functionData = FunctionEncoder.encode(function)
                val rawTransaction = RawTransaction.createTransaction(nonce,gasPrice, gasLimit, CONTRACT_ADDRESS, amount, functionData)
                val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
                val hexValue = Numeric.toHexString(signedMessage)
                it.onSuccess(hexValue)
            }catch (e: Exception){
                e.printStackTrace()
                it.onSuccess("")
            }
        }
    }

    override fun createUnVoteData(
        candidate: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ): Single<String>? {
        return accountDA0?.getActiveAccount()
            ?.flatMap {
                createUnVoteDataWithAccount(candidate, amount, gasPrice, gasLimit,it)
            }
    }

    private fun createUnVoteDataWithAccount(
        candidate: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        activeAccount: EntityAccount
    ): Single<String>{
        return Single.create{
            try {
                val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData))
                val account = EntityWalletKey.readFromString(wallet)

                if (!WalletUtil.isValidPrivateKey(account?.privateKey)){
                    it.onError(InvalidPrivateKeyException())
                    return@create
                }
                val credentials = Credentials.create(account?.privateKey)
                val from = credentials.address
                val ethGetTransactionCount = web3j?.ethGetTransactionCount(
                    from, DefaultBlockParameterName.LATEST)?.sendAsync()?.get()

                val nonce = ethGetTransactionCount?.transactionCount
                val function = Function(
                    "unvote",
                    listOf(Address(candidate), Uint256(amount)),
                    Collections.emptyList())
                val functionData = FunctionEncoder.encode(function)
                val rawTransaction = RawTransaction.createTransaction(nonce,gasPrice, gasLimit, CONTRACT_ADDRESS, BigInteger.ZERO, functionData)
                val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
                it.onSuccess(Numeric.toHexString(signedMessage))
            }catch (e: Exception){
                e.printStackTrace()
                it.onSuccess("")
            }
        }
    }


    override fun createWithdrawData(
        
        blockNumber: BigInteger,
        index: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ): Single<String>? {
        return accountDA0?.getActiveAccount()
            ?.flatMap {
                createWithdrawData(blockNumber, index, gasPrice, gasLimit,it)
            }
    }

    private fun createWithdrawData(

        blockNumber: BigInteger,
        index: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        activeAccount: EntityAccount
    ): Single<String>{
        return Single.create{
            try {
                val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData))
                val account = EntityWalletKey.readFromString(wallet)

                if (!WalletUtil.isValidPrivateKey(account?.privateKey)){
                    it.onError(InvalidPrivateKeyException())
                    return@create
                }
                val credentials = Credentials.create(account?.privateKey)
                val from = credentials.address
                val ethGetTransactionCount = web3j?.ethGetTransactionCount(
                    from, DefaultBlockParameterName.LATEST)?.sendAsync()?.get()

                val nonce = ethGetTransactionCount?.transactionCount
                val function = Function(
                    "withdraw",
                    listOf(Uint256(blockNumber), Uint256(index)),
                    Collections.emptyList())
                val functionData = FunctionEncoder.encode(function)
                val rawTransaction = RawTransaction.createTransaction(nonce,gasPrice, gasLimit, CONTRACT_ADDRESS, BigInteger.ZERO, functionData)
                val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
                val hexValue = Numeric.toHexString(signedMessage)
                it.onSuccess(hexValue)
            }catch (e: Exception){
                e.printStackTrace()
                it.onSuccess("")
            }
        }
    }

    override fun createProposeData(
        coinBaseAddress: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ): Single<String>? {
        return accountDA0?.getActiveAccount()
            ?.flatMap {
                createProposeDataWithAccount(coinBaseAddress, amount, gasPrice, gasLimit,it)
            }
    }

    private fun createProposeDataWithAccount(
        coinBaseAddress: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        activeAccount: EntityAccount
    ): Single<String> {
        return Single.create{
            try {
                val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData))
                val account = EntityWalletKey.readFromString(wallet)

                if (!WalletUtil.isValidPrivateKey(account?.privateKey)){
                    it.onError(InvalidPrivateKeyException())
                    return@create
                }
                val credentials = Credentials.create(account?.privateKey)
                val from = credentials.address
                val ethGetTransactionCount = web3j?.ethGetTransactionCount(
                    from, DefaultBlockParameterName.LATEST)?.sendAsync()?.get()

                val nonce = ethGetTransactionCount?.transactionCount
                val function = Function(
                    "propose",
                    listOf(Address(coinBaseAddress)),
                    Collections.emptyList())
                val functionData = FunctionEncoder.encode(function)
                val rawTransaction = RawTransaction.createTransaction(nonce,gasPrice, gasLimit, CONTRACT_ADDRESS, amount, functionData)
                val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
                val hexValue = Numeric.toHexString(signedMessage)
                it.onSuccess(hexValue)
            }catch (e: Exception){
                e.printStackTrace()
                it.onSuccess("")
            }
        }

    }


    override fun createResignData(
        coinBaseAddress: String,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ): Single<String>? {
        return accountDA0?.getActiveAccount()
            ?.flatMap {
                createResignDataWithAccount(coinBaseAddress, gasPrice, gasLimit,it)
            }
    }

    private fun createResignDataWithAccount(
        coinBaseAddress: String,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        activeAccount: EntityAccount
    ): Single<String> {
        return Single.create {
            try {
                val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData))
                val account = EntityWalletKey.readFromString(wallet)

                if (!WalletUtil.isValidPrivateKey(account?.privateKey)){
                    it.onError(InvalidPrivateKeyException())
                    return@create
                }
                val credentials = Credentials.create(account?.privateKey)
                val from = credentials.address
                val ethGetTransactionCount = web3j?.ethGetTransactionCount(
                    from, DefaultBlockParameterName.LATEST)?.sendAsync()?.get()

                val nonce = ethGetTransactionCount?.transactionCount
                val function = Function(
                    "resign",
                    listOf(Address(coinBaseAddress)),
                    Collections.emptyList())
                val functionData = FunctionEncoder.encode(function)
                val rawTransaction = RawTransaction.createTransaction(nonce,gasPrice, gasLimit, CONTRACT_ADDRESS, BigInteger.ZERO, functionData)
                val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
                val hexValue = Numeric.toHexString(signedMessage)
                it.onSuccess(hexValue)
            }catch (e: Exception){
                e.printStackTrace()
                it.onSuccess("")
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun vote(
        
        candidate: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        callback: TransactionListener?
    ) {
        val data = createVoteData( candidate, amount, gasPrice, gasLimit)
        data?.subscribe({
            if (it.isEmpty()) {
                callback?.onTransactionError(Exception("Null signed data"))
                return@subscribe
            }
            web3j?.ethSendRawTransaction(it)?.flowable()
                ?.subscribe(
                    { tx ->
                        val hash = tx.transactionHash
                        callback?.onTransactionCreated(hash)
                    },
                    {th ->
                        th.printStackTrace()
                        callback?.onTransactionError(Exception(th.localizedMessage))
                    }
                )
        },{
            callback?.onTransactionError(Exception(it.localizedMessage))
        })
    }

    @SuppressLint("CheckResult")
    override fun unVote(
        candidate: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        callback: TransactionListener?
    ) {
        createUnVoteData(candidate, amount, gasPrice, gasLimit)
            ?.subscribe(
                {
                    if (it.isEmpty()) {
                        callback?.onTransactionError(Exception("Null signed data"))
                    }
                    web3j?.ethSendRawTransaction(it)?.flowable()
                        ?.subscribe(
                            {tx ->
                                val hash = tx.transactionHash
                                callback?.onTransactionCreated(hash)
                            },
                            {th ->
                                th.printStackTrace()
                                callback?.onTransactionError(Exception(th.localizedMessage))
                            }
                        )
                },{
                    callback?.onTransactionError(Exception(it.localizedMessage))
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun withdraw(
        
        blockNumber: BigInteger,
        index: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        callback: TransactionListener?
    ) {
        createWithdrawData(blockNumber, index, gasPrice, gasLimit)
            ?.subscribe(
                {
                    if (it.isEmpty()) {
                        callback?.onTransactionError(Exception("Null signed data"))
                    }
                    web3j?.ethSendRawTransaction(it)?.flowable()
                        ?.subscribe(
                            {tx ->
                                val hash = tx.transactionHash
                                callback?.onTransactionCreated(hash)
                            },
                            {th ->
                                th.printStackTrace()
                                callback?.onTransactionError(Exception(th.localizedMessage))
                            }
                        )
                },
                {
                    callback?.onTransactionError(Exception(it.localizedMessage))
                }
            )
    }


    @SuppressLint("CheckResult")
    override fun propose(
        
        coinBaseAddress: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        callback: TransactionListener?
    ) {
        createProposeData(coinBaseAddress, amount, gasPrice, gasLimit)
            ?.subscribe(
                {

                    if (it.isEmpty()) {
                        callback?.onTransactionError(Exception("Null signed data"))

                    }
                    web3j?.ethSendRawTransaction(it)?.flowable()
                        ?.subscribe(
                            {tx ->
                                val hash = tx.transactionHash
                                callback?.onTransactionCreated(hash)
                            },
                            {th ->
                                th.printStackTrace()
                                callback?.onTransactionError(Exception(th.localizedMessage))
                            }
                        )
                },{
                    callback?.onTransactionError(Exception(it.localizedMessage))
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun resign(
        
        coinBaseAddress: String,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        callback: TransactionListener?
    ) {
        createResignData(coinBaseAddress, gasPrice, gasLimit)
            ?.subscribe(
                {

                    if (it.isEmpty()) {
                        callback?.onTransactionError(Exception("Null signed data"))

                    }
                    web3j?.ethSendRawTransaction(it)?.flowable()
                        ?.subscribe(
                            {tx ->
                                val hash = tx.transactionHash
                                callback?.onTransactionCreated(hash)
                            },
                            {th ->
                                th.printStackTrace()
                                callback?.onTransactionError(Exception(th.localizedMessage))
                            }
                        )
                },{
                    callback?.onTransactionError(Exception(it.localizedMessage))
                }
            )
    }
}