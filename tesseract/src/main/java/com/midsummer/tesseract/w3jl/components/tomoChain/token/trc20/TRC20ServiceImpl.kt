package com.midsummer.tesseract.w3jl.components.tomoChain.token.trc20

import android.annotation.SuppressLint
import com.midsummer.tesseract.common.exception.DefaultAccountNotFoundException
import com.midsummer.tesseract.common.exception.InvalidPrivateKeyException
import com.midsummer.tesseract.habak.EncryptedModel
import com.midsummer.tesseract.habak.cryptography.Habak
import com.midsummer.tesseract.room.entity.account.AccountDAO
import com.midsummer.tesseract.w3jl.constant.chain.Chain
import com.midsummer.tesseract.w3jl.constant.chain.CommonChain
import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.entity.EntityWalletKey
import com.midsummer.tesseract.w3jl.listener.TransactionListener
import com.midsummer.tesseract.w3jl.utils.WalletUtil
import io.reactivex.Single
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint8
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.response.Callback
import org.web3j.tx.response.QueuingTransactionReceiptProcessor
import org.web3j.utils.Numeric
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Created by cityme on 23,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class TRC20ServiceImpl(var accountDA0: AccountDAO?, var habak: Habak?, var web3j: Web3j?, var chain: Chain?) : TRC20Service {


    override fun getBalance(address: String, tokenAddress: String): Single<BigDecimal> {
        return Single.create{ emitter ->
            try {
                val function = Function(
                    "balanceOf",
                    listOf(Address(address)),
                    listOf(object : TypeReference<Uint256>() {

                    }))
                val responseValue = callSmartContractFunction(function, tokenAddress, address)
                val response = FunctionReturnDecoder.decode(
                    responseValue, function.outputParameters)
                if (response.size == 1) {
                    emitter.onSuccess(BigDecimal((response[0] as Uint256).value))
                } else {
                    emitter.onSuccess(BigDecimal.ZERO)
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    override fun getName(address: String, tokenAddress: String): Single<String> {

        return Single.create{ emitter ->
            try{
                val function = Function(
                    "name",
                    emptyList(),
                    listOf(object : TypeReference<Utf8String>() {

                    }))
                val responseValue = callSmartContractFunction(function, tokenAddress, address)
                val response = FunctionReturnDecoder.decode(
                    responseValue, function.outputParameters)
                if (response.size == 1) {
                    emitter.onSuccess((response[0] as Utf8String).value)
                } else {
                    emitter.onSuccess("Unknown name!")
                }
            }catch(t: Throwable){
                emitter.onError(t)
            }
        }
    }

    override fun getSymbol(address: String, tokenAddress: String): Single<String> {

        return Single.create{ emitter ->
            try{
                val function = Function(
                    "symbol",
                    emptyList(),
                    listOf(object : TypeReference<Utf8String>() {

                    }))
                val responseValue = callSmartContractFunction(function, tokenAddress, address)
                val response = FunctionReturnDecoder.decode(
                    responseValue, function.outputParameters)
                if (response.size == 1) {
                    emitter.onSuccess(String((response[0] as Utf8String).value.toByteArray()))
                } else {
                    emitter.onSuccess("Unknown name!")
                }
            }catch(t: Throwable){
                emitter.onError(t)
            }
        }
    }

    override fun getDecimal(address: String, tokenAddress: String): Single<Int> {

        return Single.create{ emitter ->
            try{
                val function = Function(
                    "decimals",
                    emptyList(),
                    listOf(object : TypeReference<Uint8>() {

                    }))
                val responseValue = callSmartContractFunction(function, tokenAddress, address)
                val response = FunctionReturnDecoder.decode(
                    responseValue, function.outputParameters)
                if (response.size == 1) {
                    emitter.onSuccess(BigDecimal((response[0] as Uint8).value).intValueExact())
                } else {
                    emitter.onSuccess(18)
                }
            }catch(t: Throwable){
                emitter.onError(t)
            }
        }
    }

    override fun transferToken(

        tokenAddress: String,
        recipient: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ): Single<String> {
        return Single.create { emitter ->
            /*try {
                val activeAccount = accountDA0?.getActiveAccount()?.blockingGet()
                if (activeAccount == null){
                    emitter.onError(DefaultAccountNotFoundException())
                    return@create
                }
                val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData))
                val account = EntityWalletKey.readFromString(wallet)
                if (account == null || !WalletUtil.isValidPrivateKey(account?.privateKey)){
                    emitter.onError(InvalidPrivateKeyException())
                    return@create
                }

                val credentials = Credentials.create(account?.privateKey)
                val transactionReceiptProcessor = QueuingTransactionReceiptProcessor(web3j, object : Callback {
                    override fun accept(transactionReceipt: TransactionReceipt?) {
                        //callback?.onTransactionComplete(transactionReceipt!!.transactionHash, transactionReceipt.status)
                        emitter.onSuccess(transactionReceipt!!.transactionHash)
                    }

                    override fun exception(exception: java.lang.Exception?) {
                        emitter.onError(Exception(exception?.localizedMessage))
                    }
                }, TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH * 2, TransactionManager.DEFAULT_POLLING_FREQUENCY )
                val transactionManager = RawTransactionManager(web3j,
                    credentials,
                    chain?.getChainId()?.toByte() ?: CommonChain.TOMO_CHAIN.getChainId().toByte(),
                    transactionReceiptProcessor)
                val tokenContract =
                    TRC20(
                        tokenAddress,
                        web3j,
                        transactionManager,
                        gasPrice,
                        gasLimit
                    )
                val t = tokenContract.transfer(recipient, amount).sendAsync().get()
                callback?.onTransactionCreated(t.transactionHash)

            }catch (e: Exception){
                callback?.onTransactionError(e)
            }*/
        }
    }

    @SuppressLint("CheckResult")
    override fun transferToken(
        tokenAddress: String,
        recipient: String,
        amount: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        callback: TransactionListener?
    ) {
        try {
            accountDA0?.getActiveAccount()?.subscribe(
                { activeAccount ->
                    if (activeAccount == null){
                        callback?.onTransactionError(DefaultAccountNotFoundException())
                    }
                    val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount!!.encryptedData))
                    val account = EntityWalletKey.readFromString(wallet)
                    if (account == null || !WalletUtil.isValidPrivateKey(account?.privateKey)){
                        callback?.onTransactionError(InvalidPrivateKeyException())
                        return@subscribe
                    }

                    val credentials = Credentials.create(account.privateKey)
                    val transactionReceiptProcessor = QueuingTransactionReceiptProcessor(web3j, object : Callback {
                        override fun accept(transactionReceipt: TransactionReceipt?) {
                            callback?.onTransactionComplete(transactionReceipt!!.transactionHash, transactionReceipt.status)
                        }

                        override fun exception(exception: java.lang.Exception?) {
                            callback?.onTransactionError(exception!!)
                        }
                    }, TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH * 2, TransactionManager.DEFAULT_POLLING_FREQUENCY )
                    val transactionManager = RawTransactionManager(web3j,
                        credentials,
                        chain?.getChainId()?.toByte() ?: CommonChain.TOMO_CHAIN.getChainId().toByte(),
                        transactionReceiptProcessor)
                    val tokenContract =
                        TRC20(
                            tokenAddress,
                            web3j,
                            transactionManager,
                            gasPrice,
                            gasLimit
                        )
                    val t = tokenContract.transfer(recipient, amount).sendAsync().get()
                    callback?.onTransactionCreated(t.transactionHash)
                },{
                    callback?.onTransactionError(it as Exception)
                }
            )



        }catch (e: Exception){
            callback?.onTransactionError(e)
        }
    }

    override fun estimateTokenTransferGas(tokenAddress: String): Single<BigInteger> {
        return Single.create { emitter ->
            try {

                val transaction = Transaction.createContractTransaction(tokenAddress, BigInteger.ONE,
                    BigInteger.TEN, "")

                val response = web3j?.ethEstimateGas(transaction)?.sendAsync()?.get()
                if (response != null && response.result != null){
                    try {
                        emitter.onSuccess(BigInteger(Numeric.cleanHexPrefix(response.result),16))
                    }catch (e: Exception){
                        emitter.onSuccess(BigInteger.valueOf(21000))
                    }
                }else{
                    emitter.onSuccess(BigInteger.valueOf(21000))
                }
            } catch (e: Exception) {
                emitter.tryOnError(e)
            }
        }
    }

    override fun extractFunction(src: String): TRC20FunctionType {
        return try{
            if (src.length < 10){
                TRC20Function.INVALID
            }
            val trcList = arrayListOf(
                TRC20Function.TOTAL_SUPPLY, TRC20Function.BALANCE_OF, TRC20Function.TRANSFER,
                TRC20Function.TRANSFER_FROM, TRC20Function.APPROVE, TRC20Function.ALLOWANCE
            )
            val methodSignature = src.substring(0,10)
            for (i in trcList){
                if (i.functionSignature() == methodSignature){
                    return i
                }
            }
            TRC20Function.INVALID
        }catch(e: Exception){
            TRC20Function.INVALID
        }
    }

    override fun decodeHexData(src: String): List<String> {
        return try {
            val function = extractFunction(src)
            val refMethod = TypeDecoder::class.java.getDeclaredMethod("decode",
                String::class.java, Int::class.java, Class::class.java)
            refMethod.isAccessible = true

            when(function){
                TRC20Function.TRANSFER ->{
                    if (src.length < 74) return arrayListOf()

                    val to = src.substring(10,74)
                    val value = src.substring(74)
                    val address = refMethod.invoke(null, to, 0, Address::class.java) as Address
                    val amount =refMethod.invoke(null, value, 0, Uint256::class.java) as Uint256
                    return arrayListOf(address.value, amount.value.toBigDecimal().toPlainString())
                }
            }
            arrayListOf()
        }catch (e: Exception){
            arrayListOf()
        }
    }



    private fun callSmartContractFunction(function: Function, contractAddress: String, address: String): String? {
        val encodedFunction = FunctionEncoder.encode(function)
        val response = web3j?.ethCall(
            Transaction.createEthCallTransaction(address, contractAddress, encodedFunction),
            DefaultBlockParameterName.LATEST)
            ?.sendAsync()?.get()
        return response?.value
    }


}