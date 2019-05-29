package com.midsummer.tesseract.core

import android.content.Context
import com.midsummer.tesseract.common.exception.CorruptedHabakException
import com.midsummer.tesseract.common.exception.DefaultAccountNotFoundException
import com.midsummer.tesseract.habak.EncryptedModel
import com.midsummer.tesseract.habak.HabakFactory
import com.midsummer.tesseract.habak.cryptography.Habak
import com.midsummer.tesseract.room.entity.account.DatabaseAccount
import com.midsummer.tesseract.room.entity.uniqueConfig.DatabaseUniqueConfig
import com.midsummer.tesseract.room.entity.uniqueConfig.UniqueConfigDAO
import com.midsummer.tesseract.w3jl.components.coreBlockchain.BlockChainService
import com.midsummer.tesseract.w3jl.components.coreBlockchain.BlockChainServiceImpl
import com.midsummer.tesseract.w3jl.components.signer.SignerService
import com.midsummer.tesseract.w3jl.components.signer.SignerServiceImpl
import com.midsummer.tesseract.w3jl.components.tomoChain.masterNode.TomoValidatorService
import com.midsummer.tesseract.w3jl.components.tomoChain.masterNode.TomoValidatorServiceImpl
import com.midsummer.tesseract.w3jl.components.tomoChain.token.trc20.TRC20Service
import com.midsummer.tesseract.w3jl.components.tomoChain.token.trc20.TRC20ServiceImpl
import com.midsummer.tesseract.w3jl.components.wallet.WalletService
import com.midsummer.tesseract.w3jl.components.wallet.WalletServiceImpl
import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.entity.EntityWalletKey
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import java.lang.ref.WeakReference

/**
 * Created by cityme on 24,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class Tesseract {

    private var context: WeakReference<Context>? = null
    private var config: TesseractConfig = DefaultTesseractConfig()
    private var web3j: Web3j? = null

    private var habak: Habak? = null

    private var databaseAccount: DatabaseAccount? = null
    private var databaseUniqueConfig: DatabaseUniqueConfig? = null

    private var walletService: WalletService? = null
    private var tomoValidatorService: TomoValidatorService? = null
    private var coreBlockChainService: BlockChainService? = null
    private var signerService: SignerService? = null
    private var trC20Service: TRC20Service? = null
    private var coreFunctions: CoreFunctions? = null

    companion object{
        private var instance: Tesseract? = null

        fun openTheWormHole(context: WeakReference<Context>, config: TesseractConfig = DefaultTesseractConfig()){
            instance = Tesseract()
            instance?.context = context
            instance?.config = config
            instance?.web3j = Web3j.build(HttpService(config.chain().getEndpoint()))
            instance?.habak = HabakFactory().withAlias(config.habakAlias()).build()
            instance?.databaseAccount = context.get()?.let { DatabaseAccount.getInstance(it) }
            instance?.databaseUniqueConfig = context.get()?.let { DatabaseUniqueConfig.getInstance(it) }
            instance?.walletService = WalletServiceImpl()

        }

        fun changeConfig(config: TesseractConfig){
            instance?.config = config
            instance?.trC20Service = null
            instance?.signerService = null
            instance?.coreBlockChainService = null
            instance?.tomoValidatorService = null
            instance?.coreFunctions = null
        }

        fun getInstance() : Tesseract?{
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }

    fun getUniqueConfigDatabase() : UniqueConfigDAO?{
        return databaseUniqueConfig?.uniqueConfigDAO()
    }


    fun getCoreFunctions() : CoreFunctions?{
        if (coreFunctions == null){
            coreFunctions = CoreFunctionsImpl(habak, databaseAccount, walletService)
        }
        return coreFunctions
    }


    fun getSignerService() : SignerService?{
        if (signerService == null){
            val activeAccount = databaseAccount?.accountDAO()?.getActiveAccount()?.blockingGet() ?: throw DefaultAccountNotFoundException()
            val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData)) ?: throw CorruptedHabakException()
            val entityWallet = EntityWalletKey.readFromString(wallet) ?: throw CorruptedHabakException()
            signerService =  SignerServiceImpl(entityWallet, web3j)
        }
        return signerService
    }

    fun getTomoValidatorService() : TomoValidatorService?{
        if (tomoValidatorService == null){
            val activeAccount = databaseAccount?.accountDAO()?.getActiveAccount()?.blockingGet() ?: throw DefaultAccountNotFoundException()
            val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData)) ?: throw CorruptedHabakException()
            val entityWallet = EntityWalletKey.readFromString(wallet) ?: throw CorruptedHabakException()
            tomoValidatorService =  TomoValidatorServiceImpl(entityWallet, web3j)
        }
        return tomoValidatorService

    }

    fun getTRC20Service() : TRC20Service?{
        if (trC20Service == null){
            val activeAccount = databaseAccount?.accountDAO()?.getActiveAccount()?.blockingGet() ?: throw DefaultAccountNotFoundException()
            val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData)) ?: throw CorruptedHabakException()
            val entityWallet = EntityWalletKey.readFromString(wallet) ?: throw CorruptedHabakException()
            trC20Service = TRC20ServiceImpl(entityWallet, web3j, config.chain())
        }
        return trC20Service
    }

    fun getCoreBlockChainService() : BlockChainService?{
        if (coreBlockChainService == null){

            val activeAccount = databaseAccount?.accountDAO()?.getActiveAccount()?.blockingGet() ?: throw DefaultAccountNotFoundException()
            val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData)) ?: throw CorruptedHabakException()
            val entityWallet = EntityWalletKey.readFromString(wallet) ?: throw CorruptedHabakException()
            coreBlockChainService =  BlockChainServiceImpl(entityWallet, web3j)
        }
        return coreBlockChainService

    }


}