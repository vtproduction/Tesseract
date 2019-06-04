package com.midsummer.tesseract.core

import android.content.Context
import com.midsummer.tesseract.common.dagger.ApplicationComponent
import com.midsummer.tesseract.common.dagger.ApplicationModule
import com.midsummer.tesseract.common.dagger.DaggerApplicationComponent
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
import javax.inject.Inject

/**
 * Created by cityme on 24,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class Tesseract {

    private var context: WeakReference<Context>? = null
    private var config: TesseractConfig = DefaultTesseractConfig()

    @Inject lateinit var databaseAccount: DatabaseAccount
    @Inject lateinit var databaseUniqueConfig: DatabaseUniqueConfig
    @Inject lateinit var walletService: WalletService
    @Inject lateinit var tomoValidatorService: TomoValidatorService
    @Inject lateinit var coreBlockChainService: BlockChainService
    @Inject lateinit var signerService: SignerService
    @Inject lateinit var trC20Service: TRC20Service
    @Inject lateinit var coreFunctions : CoreFunctions



    companion object{
        private var instance: Tesseract? = null

        fun openTheWormHole(context: WeakReference<Context>, config: TesseractConfig = DefaultTesseractConfig()){
            instance = Tesseract()
            instance?.context = context
            instance?.config = config
            instance?.getApplicationComponent()?.inject(instance)

        }

        fun changeConfig(config: TesseractConfig){
            instance?.config = config
        }

        fun getInstance() : Tesseract?{
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }


    private fun getApplicationComponent() : ApplicationComponent{
        return DaggerApplicationComponent.builder()
            .applicationModule(context?.let { ApplicationModule(it, config) }).build()
    }
}