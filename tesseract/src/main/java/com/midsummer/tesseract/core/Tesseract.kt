package com.midsummer.tesseract.core

import android.content.Context
import com.midsummer.tesseract.common.exception.CorruptedHabakException
import com.midsummer.tesseract.common.exception.DefaultAccountNotFoundException
import com.midsummer.tesseract.habak.EncryptedModel
import com.midsummer.tesseract.habak.HabakFactory
import com.midsummer.tesseract.habak.cryptography.Habak
import com.midsummer.tesseract.room.entity.account.DatabaseAccount
import com.midsummer.tesseract.room.entity.uniqueConfig.DatabaseUniqueConfig
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
    /*private var tomoValidatorService: TomoValidatorService? = null
    private var coreBlockChainService: CoreBlockChainService? = null
    private var signerService: SignerService? = null*/


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

        fun getInstance() : Tesseract?{
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }


    fun getSignerService() : SignerService?{
        val activeAccount = databaseAccount?.accountDAO()?.getActiveAccount()?.blockingGet() ?: throw DefaultAccountNotFoundException()
        val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData)) ?: throw CorruptedHabakException()
        val entityWallet = EntityWallet.readFromString(wallet) ?: throw CorruptedHabakException()
        return SignerServiceImpl(entityWallet, web3j)
    }

    fun getTomoValidatorService() : TomoValidatorService?{
        val activeAccount = databaseAccount?.accountDAO()?.getActiveAccount()?.blockingGet() ?: throw DefaultAccountNotFoundException()
        val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData)) ?: throw CorruptedHabakException()
        val entityWallet = EntityWallet.readFromString(wallet) ?: throw CorruptedHabakException()
        return TomoValidatorServiceImpl(entityWallet, web3j)
    }

    fun getTRC20Service() : TRC20Service?{
        val activeAccount = databaseAccount?.accountDAO()?.getActiveAccount()?.blockingGet() ?: throw DefaultAccountNotFoundException()
        val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData)) ?: throw CorruptedHabakException()
        val entityWallet = EntityWallet.readFromString(wallet) ?: throw CorruptedHabakException()
        return TRC20ServiceImpl(entityWallet, web3j, config.chain())
    }

    fun getCoreBlockChainService() : BlockChainService?{
        val activeAccount = databaseAccount?.accountDAO()?.getActiveAccount()?.blockingGet() ?: throw DefaultAccountNotFoundException()
        val wallet = habak?.decrypt(EncryptedModel.readFromString(activeAccount.encryptedData)) ?: throw CorruptedHabakException()
        val entityWallet = EntityWallet.readFromString(wallet) ?: throw CorruptedHabakException()
        return BlockChainServiceImpl(entityWallet, web3j)
    }


}