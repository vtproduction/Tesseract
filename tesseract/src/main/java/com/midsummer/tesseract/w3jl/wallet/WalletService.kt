package com.midsummer.tesseract.w3jl.wallet

import com.midsummer.tesseract.w3jl.constant.chain.CommonChain
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
                                  chainId: Int? = CommonChain.TOMO_CHAIN.getChainId()) : Single<EntityWallet?>

    fun createWalletFromPrivateKey(privateKey: String,
                                   walletName: String? = "",
                                   chainId: Int? = CommonChain.TOMO_CHAIN.getChainId()) : Single<EntityWallet?>

    fun createWalletFromAddress(address: String,
                                walletName: String? = "",
                                chainId: Int? = CommonChain.TOMO_CHAIN.getChainId()) : Single<EntityWallet?>
}