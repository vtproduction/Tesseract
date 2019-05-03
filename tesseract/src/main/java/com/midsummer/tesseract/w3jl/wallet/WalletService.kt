package com.midsummer.tesseract.w3jl.wallet

/**
 * Created by NienLe on 2019-05-03,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
interface WalletService {

    fun getWordList() : List<String>
    fun generateMnemonics() : String
    fun createWalletFromMnemonics(mnemonic: String) : EntityWallet
    fun createWalletFromPrivateKey(privateKey: String) : EntityWallet

}