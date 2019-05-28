package com.midsummer.tesseract.w3jl.components.tomoChain.masterNode

import android.annotation.SuppressLint
import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.listener.TransactionListener
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
class TomoValidatorServiceImpl(var account: EntityWallet?,  var web3j: Web3j?) : TomoValidatorService {

    private val CONTRACT_ADDRESS = "0x0000000000000000000000000000000000000088"

    override fun createVoteData(
        
        candidate: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ): String {
        return try {
            val credentials = Credentials.create(account?.privateKey) ?: return ""
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
            hexValue
        }catch (e: Exception){
            e.printStackTrace()
            ""
        }
    }

    override fun createUnVoteData(
        
        candidate: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ): String {
        return try {
            val credentials = Credentials.create(account?.privateKey) ?: return ""
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
            val hexValue = Numeric.toHexString(signedMessage)
            hexValue
        }catch (e: Exception){
            e.printStackTrace()
            ""
        }
    }

    override fun createWithdrawData(
        
        blockNumber: BigInteger,
        index: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ): String {
        return try {
            val credentials = Credentials.create(account?.privateKey) ?: return ""
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
            hexValue
        }catch (e: Exception){
            e.printStackTrace()
            ""
        }
    }

    override fun createProposeData(
        
        coinBaseAddress: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ): String {
        return try {
            val credentials = Credentials.create(account?.privateKey) ?: return ""
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
            hexValue
        }catch (e: Exception){
            e.printStackTrace()
            ""
        }
    }

    override fun createResignData(
        
        coinBaseAddress: String,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ): String {
        return try {
            val credentials = Credentials.create(account?.privateKey) ?: return ""
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
            hexValue
        }catch (e: Exception){
            e.printStackTrace()
            ""
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
        try{
            val data = createVoteData( candidate, amount, gasPrice, gasLimit)
            if (data.isEmpty()) {
                callback?.onTransactionError(Exception("Null signed data"))
                return
            }
            web3j?.ethSendRawTransaction(data)?.flowable()
                ?.subscribe(
                    {
                        val hash = it.transactionHash
                        callback?.onTransactionCreated(hash)
                    },
                    {
                        it.printStackTrace()
                        callback?.onTransactionError(Exception(it.localizedMessage))
                    }
                )
        }catch(e: Exception){
            e.printStackTrace()
            callback?.onTransactionError(e)
        }
    }

    @SuppressLint("CheckResult")
    override fun unVote(
        
        candidate: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        callback: TransactionListener?
    ) {
        try{
            val data = createUnVoteData(candidate, amount, gasPrice, gasLimit)
            if (data.isEmpty()) {
                callback?.onTransactionError(Exception("Null signed data"))
                return
            }
            web3j?.ethSendRawTransaction(data)?.flowable()
                ?.subscribe(
                    {
                        val hash = it.transactionHash
                        callback?.onTransactionCreated(hash)
                    },
                    {
                        it.printStackTrace()
                        callback?.onTransactionError(Exception(it.localizedMessage))
                    }
                )
        }catch(e: Exception){
            e.printStackTrace()
            callback?.onTransactionError(e)
        }
    }

    @SuppressLint("CheckResult")
    override fun withdraw(
        
        blockNumber: BigInteger,
        index: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        callback: TransactionListener?
    ) {
        try{
            val data = createWithdrawData(blockNumber, index, gasPrice, gasLimit)
            if (data.isEmpty()) {
                callback?.onTransactionError(Exception("Null signed data"))
                return
            }
            web3j?.ethSendRawTransaction(data)?.flowable()
                ?.subscribe(
                    {
                        val hash = it.transactionHash
                        callback?.onTransactionCreated(hash)
                    },
                    {
                        it.printStackTrace()
                        callback?.onTransactionError(Exception(it.localizedMessage))
                    }
                )
        }catch(e: Exception){
            e.printStackTrace()
            callback?.onTransactionError(e)
        }
    }

    @SuppressLint("CheckResult")
    override fun propose(
        
        coinBaseAddress: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        callback: TransactionListener?
    ) {
        try{
            val data = createProposeData(coinBaseAddress, amount, gasPrice, gasLimit)
            if (data.isEmpty()) {
                callback?.onTransactionError(Exception("Null signed data"))
                return
            }
            web3j?.ethSendRawTransaction(data)?.flowable()
                ?.subscribe(
                    {
                        val hash = it.transactionHash
                        callback?.onTransactionCreated(hash)
                    },
                    {
                        it.printStackTrace()
                        callback?.onTransactionError(Exception(it.localizedMessage))
                    }
                )
        }catch(e: Exception){
            e.printStackTrace()
            callback?.onTransactionError(e)
        }
    }

    @SuppressLint("CheckResult")
    override fun resign(
        
        coinBaseAddress: String,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        callback: TransactionListener?
    ) {
        try{
            val data = createResignData(coinBaseAddress, gasPrice, gasLimit)
            if (data.isEmpty()) {
                callback?.onTransactionError(Exception("Null signed data"))
                return
            }
            web3j?.ethSendRawTransaction(data)?.flowable()
                ?.subscribe(
                    {
                        val hash = it.transactionHash
                        callback?.onTransactionCreated(hash)
                    },
                    {
                        it.printStackTrace()
                        callback?.onTransactionError(Exception(it.localizedMessage))
                    }
                )
        }catch(e: Exception){
            e.printStackTrace()
            callback?.onTransactionError(e)
        }
    }
}