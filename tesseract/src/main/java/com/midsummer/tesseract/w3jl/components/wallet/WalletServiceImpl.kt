package com.midsummer.tesseract.w3jl.components.wallet

import android.util.Log
import com.midsummer.tesseract.common.LogTag
import com.midsummer.tesseract.common.exception.InvalidAddressException
import com.midsummer.tesseract.common.exception.InvalidMnemonicException
import com.midsummer.tesseract.common.exception.InvalidPrivateKeyException
import com.midsummer.tesseract.w3jl.constant.WalletSource
import com.midsummer.tesseract.w3jl.constant.chain.CommonChain
import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.entity.EntityWalletKey
import io.reactivex.Single
import org.bitcoinj.crypto.ChildNumber
import org.bitcoinj.crypto.HDKeyDerivation
import org.bitcoinj.wallet.DeterministicSeed
import org.web3j.crypto.*
import org.web3j.utils.Numeric
import java.security.SecureRandom

/**
 * Created by NienLe on 2019-05-09,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class WalletServiceImpl : WalletService {

    override fun getWordList(): List<String> {
        return try{
            MnemonicUtils.getWords()
        }catch(e: Exception){
            Log.e(LogTag.TAG_W3JL, "getWordList",e)
            arrayListOf()
        }
    }

    override fun generateMnemonics(): String {
        return try{
            val initialEntropy = ByteArray(16)
            val secureRandom =  SecureRandom()
            secureRandom.nextBytes(initialEntropy)
            MnemonicUtils.generateMnemonic(initialEntropy)
        }catch(e: Exception){
            Log.e(LogTag.TAG_W3JL, "generateMnemonics",e)
            ""
        }
    }

    override fun createWalletFromMnemonics(mnemonic: String,
                                           hdPath: String,
                                           walletName: String?,
                                           chainId: Int?): Single<EntityWalletKey?> {
        return Single.create {
            try{
                val pathArray = hdPath.split("/".toRegex()).dropLastWhile {path -> path.isEmpty() }.toTypedArray()
                val passphrase = ""
                val list = mnemonic.split(" ")
                if (list.size != 12){
                    it.onError(InvalidMnemonicException())
                    return@create
                }
                val creationTimeSeconds = System.currentTimeMillis() / 1000
                val ds = DeterministicSeed(list, null, passphrase, creationTimeSeconds)
                val seedBytes = ds.seedBytes
                var dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes)
                for (i in 1 until pathArray.size) {
                    val childNumber: ChildNumber
                    childNumber = if (pathArray[i].endsWith("'")) {
                        val number = Integer.parseInt(pathArray[i].substring(0,
                            pathArray[i].length - 1))
                        ChildNumber(number, true)
                    } else {
                        val number = Integer.parseInt(pathArray[i])
                        ChildNumber(number, false)
                    }
                    dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber)
                }
                val keyPair = ECKeyPair.create(dkKey.privKeyBytes)
                val privateKey = Numeric.toHexStringNoPrefixZeroPadded(keyPair.privateKey, Keys.PRIVATE_KEY_LENGTH_IN_HEX)
                val publicKey = Numeric.toHexStringNoPrefixZeroPadded(keyPair.privateKey, 128)

                val c = Credentials.create(privateKey)
                val wallet = EntityWalletKey()

                wallet.createdBy = WalletSource.MNEMONIC
                wallet.address = c.address
                wallet.privateKey = privateKey
                wallet.publicKey = publicKey
                it.onSuccess(wallet)
            }catch(e: Exception){
                Log.e(LogTag.TAG_W3JL, "createWalletFromMnemonics", e)
                it.onError(e)
            }
        }
    }

    override fun createWalletFromPrivateKey(privateKey: String,
                                            walletName: String?,
                                            chainId: Int?): Single<EntityWalletKey?> {
        return Single.create{
            try{
                val wallet = EntityWalletKey()
                if (!WalletUtils.isValidPrivateKey(privateKey)){
                    it.onError(InvalidPrivateKeyException())
                    return@create
                }
                wallet.createdBy = WalletSource.PRIVATE_KEY
                val c = Credentials.create(privateKey)
                val publicKey = Numeric.toHexStringNoPrefixZeroPadded(c.ecKeyPair.privateKey, 128)

                wallet.address = c.address
                wallet.privateKey = privateKey
                wallet.publicKey = publicKey
                it.onSuccess(wallet)
            }catch(e: Exception){
                Log.e(LogTag.TAG_W3JL, "createWalletFromPrivateKey",e)
                it.onError(e)
            }
        }
    }

    override fun createWalletFromAddress(address: String,
                                         walletName: String?,
                                         chainId: Int?): Single<EntityWalletKey?> {
        return Single.create{
            try{
                val wallet = EntityWalletKey()
                if (!WalletUtils.isValidAddress(address)){
                    it.tryOnError(InvalidAddressException())
                    return@create
                }
                wallet.createdBy = WalletSource.ADDRESS
                wallet.address = address
                wallet.privateKey = ""
                wallet.publicKey = ""
                it.onSuccess(wallet)
            }catch(e: Exception){
                Log.e(LogTag.TAG_W3JL, "createWalletFromPrivateKey",e)
                it.onError(e)
            }
        }
    }
}