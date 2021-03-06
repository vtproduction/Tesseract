package com.midsummer.tesseract.w3jl.components.wallet

import com.midsummer.tesseract.w3jl.constant.chain.CommonChain
import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.entity.EntityWalletKey
import io.reactivex.Single

/**
 * Created by NienLe on 2019-05-03,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
interface WalletService {

    fun getWordList() : List<String>
    fun generateMnemonics() : String
    fun createWalletFromMnemonics(mnemonic: String,
                                  hdPath: String,
                                  walletName: String? = "",
                                  chainId: Int? = CommonChain.TOMO_CHAIN.getChainId()) : Single<EntityWalletKey?>

    fun createWalletFromPrivateKey(privateKey: String,
                                   walletName: String? = "",
                                   chainId: Int? = CommonChain.TOMO_CHAIN.getChainId()) : Single<EntityWalletKey?>

    fun createWalletFromAddress(address: String,
                                walletName: String? = "",
                                chainId: Int? = CommonChain.TOMO_CHAIN.getChainId()) : Single<EntityWalletKey?>
}