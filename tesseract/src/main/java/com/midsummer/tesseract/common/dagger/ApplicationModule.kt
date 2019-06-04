package com.midsummer.tesseract.common.dagger

import android.content.Context
import com.midsummer.tesseract.core.CoreFunctions
import com.midsummer.tesseract.core.CoreFunctionsImpl
import com.midsummer.tesseract.core.DefaultTesseractConfig
import com.midsummer.tesseract.core.TesseractConfig
import com.midsummer.tesseract.habak.HabakFactory
import com.midsummer.tesseract.habak.cryptography.Habak
import com.midsummer.tesseract.room.entity.account.AccountDAO
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
import dagger.Module
import dagger.Provides
import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jService
import org.web3j.protocol.http.HttpService
import java.lang.ref.WeakReference
import javax.inject.Singleton

/**
 * Created by cityme on 03,June,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */

@Module
class ApplicationModule(var context: WeakReference<Context>, var config: TesseractConfig = DefaultTesseractConfig()) {

    @Provides
    fun getWalletService() : WalletService{
        return WalletServiceImpl()
    }

    @Provides
    fun getWeb3JService() : Web3j{
        return Web3j.build(HttpService(config.chain().getEndpoint()))
    }

    
    @Provides
    fun getHaBak() : Habak{
        return HabakFactory().withAlias(config.habakAlias()).build()
    }

    
    @Provides
    fun getDatabaseAccount() : DatabaseAccount {
        return DatabaseAccount.getInstance(context.get()!!, config.roomHelperSalt())!!
    }

    
    @Provides
    fun getDatabaseUniqueConfig() : DatabaseUniqueConfig {
        return DatabaseUniqueConfig.getInstance(context.get()!!, config.roomHelperSalt())!!
    }

    
    @Provides
    fun getCoreFunctions() : CoreFunctions{
        return CoreFunctionsImpl(getHaBak(), getDatabaseAccount(), getWalletService())
    }

    
    @Provides
    fun getSignerService() : SignerService{
        return SignerServiceImpl(getDatabaseAccount().accountDAO(), getHaBak(), getWeb3JService())
    }

    
    @Provides
    fun getCoreBlockChainService() : BlockChainService{
        return BlockChainServiceImpl(getDatabaseAccount().accountDAO(), getHaBak(), getWeb3JService())
    }

    
    @Provides
    fun getTomoValidatorService() : TomoValidatorService{
        return TomoValidatorServiceImpl(getDatabaseAccount().accountDAO(), getHaBak(), getWeb3JService())
    }

    
    @Provides
    fun getTRC20Service() : TRC20Service{
        return TRC20ServiceImpl(getDatabaseAccount().accountDAO(), getHaBak(), getWeb3JService(), config.chain())
    }
}