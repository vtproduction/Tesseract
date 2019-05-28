package com.midsummer.tesseract.core

import com.midsummer.tesseract.common.exception.*
import com.midsummer.tesseract.habak.EncryptedModel
import com.midsummer.tesseract.habak.cryptography.Habak
import com.midsummer.tesseract.room.entity.account.DatabaseAccount
import com.midsummer.tesseract.room.entity.account.EntityAccount
import com.midsummer.tesseract.w3jl.components.wallet.WalletService
import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.utils.WalletUtil
import io.reactivex.Single
import io.reactivex.functions.Function3

/**
 * Created by cityme on 28,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */

class CoreFunctionsImpl(
    var habak: Habak?,
    var dbAccount: DatabaseAccount?,
    var walletService: WalletService?
) : CoreFunctions{


    override fun createAccountFromMnemonics(mnemonics: String?, hdPath: String): Single<Pair<String?, Throwable?>> {
        return Single.create {
            try{
                if (habak == null || dbAccount == null || walletService == null) {
                    it.onSuccess(Pair(null, NullPointerException()))
                    return@create
                }

                if (!WalletUtil.isValidMnemonics(mnemonics, false)){
                    it.onSuccess(Pair(null, InvalidMnemonicException()))
                    return@create
                }
                val entityWallet = walletService?.createWalletFromMnemonics(mnemonics!!, hdPath)?.blockingGet()

                if (entityWallet == null) {
                    it.onSuccess(Pair(null, NullPointerException()))
                    return@create
                }

                if (dbAccount!!.accountDAO().getAccountByAddress(entityWallet.address).blockingGet() != null){
                    it.onSuccess(Pair(null, AccountAlreadyExistedException()))
                    return@create
                }

                val encryptedData = habak!!.encrypt(entityWallet.writeToString())
                val entityAccount = EntityAccount(
                    entityWallet.address, false, encryptedData.toByteArrayString(), System.currentTimeMillis(), System.currentTimeMillis())

                dbAccount!!.accountDAO().addAccount(entityAccount)
                it.onSuccess(Pair(entityWallet.address, null))

            }catch(t: Throwable){
                it.onSuccess(Pair(null, t))
            }
        }
    }

    override fun createAccountFromPrivateKey(privateKey: String?): Single<Pair<String?, Throwable?>> {
        return Single.create {
            try{
                if (habak == null || dbAccount == null || walletService == null) {
                    it.onSuccess(Pair(null, NullPointerException()))
                    return@create
                }

                if (!WalletUtil.isValidPrivateKey(privateKey)){
                    it.onSuccess(Pair(null, InvalidPrivateKeyException()))
                    return@create
                }
                val entityWallet = walletService?.createWalletFromPrivateKey(privateKey!!)?.blockingGet()

                if (entityWallet == null) {
                    it.onSuccess(Pair(null, NullPointerException()))
                    return@create
                }

                if (dbAccount!!.accountDAO().getAccountByAddress(entityWallet.address).blockingGet() != null){
                    it.onSuccess(Pair(null, AccountAlreadyExistedException()))
                    return@create
                }

                val encryptedData = habak!!.encrypt(entityWallet.writeToString())
                val entityAccount = EntityAccount(
                    entityWallet.address, false, encryptedData.toByteArrayString(), System.currentTimeMillis(), System.currentTimeMillis())

                dbAccount!!.accountDAO().addAccount(entityAccount)
                it.onSuccess(Pair(entityWallet.address, null))

            }catch(t: Throwable){
                it.onSuccess(Pair(null, t))
            }
        }
    }

    override fun createAccountFromAddress(address: String?): Single<Pair<String?, Throwable?>> {
        return Single.create {
            try{
                if (habak == null || dbAccount == null || walletService == null) {
                    it.onSuccess(Pair(null, NullPointerException()))
                    return@create
                }

                if (!WalletUtil.isValidAddress(address)){
                    it.onSuccess(Pair(null, InvalidMnemonicException()))
                    return@create
                }
                val entityWallet = walletService?.createWalletFromAddress(address!!)?.blockingGet()

                if (entityWallet == null) {
                    it.onSuccess(Pair(null, NullPointerException()))
                    return@create
                }

                if (dbAccount!!.accountDAO().getAccountByAddress(entityWallet.address).blockingGet() != null){
                    it.onSuccess(Pair(null, AccountAlreadyExistedException()))
                    return@create
                }

                val encryptedData = habak!!.encrypt(entityWallet.writeToString())
                val entityAccount = EntityAccount(
                    entityWallet.address, false, encryptedData.toByteArrayString(), System.currentTimeMillis(), System.currentTimeMillis())

                dbAccount!!.accountDAO().addAccount(entityAccount)
                it.onSuccess(Pair(entityWallet.address, null))

            }catch(t: Throwable){
                it.onSuccess(Pair(null, t))
            }
        }
    }

    override fun getActiveAccount(): Single<Pair<EntityWallet?, Throwable?>> {
        return Single.create {
            try{
                if (habak == null || dbAccount == null || walletService == null) {
                    it.onSuccess(Pair(null, NullPointerException()))
                    return@create
                }

                val firstAccount = dbAccount!!.accountDAO().getFirstAccount().blockingGet()
                val activeAccount = dbAccount!!.accountDAO().getActiveAccount().blockingGet() ?: firstAccount

                if (activeAccount == null){
                    it.onSuccess(Pair(null, DefaultAccountNotFoundException()))
                    return@create
                }

                val encryptData = activeAccount.encryptedData
                val walletSrc = habak!!.decrypt(EncryptedModel.readFromString(encryptData))
                if (walletSrc == null){
                    it.onSuccess(Pair(null, CorruptedHabakException()))
                    return@create
                }

                val entityWallet = EntityWallet.readFromString(walletSrc)
                if (entityWallet == null){
                    it.onSuccess(Pair(null, CorruptedHabakException()))
                    return@create
                }
                entityWallet.privateKey = ""
                entityWallet.dataSource = ""


                it.onSuccess(Pair(entityWallet, null))

            }catch(t: Throwable){
                it.onSuccess(Pair(null, t))
            }
        }
    }

    override fun getAllAccount(): Single<Pair<MutableList<EntityWallet>?, Throwable?>> {
        return Single.create {
            try{
                if (habak == null || dbAccount == null || walletService == null) {
                    it.onSuccess(Pair(null, NullPointerException()))
                    return@create
                }

                val list : MutableList<EntityWallet> = arrayListOf()

                dbAccount!!.accountDAO().getAllAccounts().blockingGet()
                    .forEach { a ->
                        val encryptData = a.encryptedData
                        val walletSrc = habak!!.decrypt(EncryptedModel.readFromString(encryptData))
                        val entityWallet = EntityWallet.readFromString(walletSrc)

                        if (entityWallet != null){
                            entityWallet.privateKey = ""
                            entityWallet.dataSource = ""
                            entityWallet.metadata = if (a.isSelected) "1" else "0"
                            list.add(entityWallet)
                        }

                    }
                it.onSuccess(Pair(list, null))
            }catch(t: Throwable){
                it.onSuccess(Pair(null, t))
            }
        }
    }

    override fun getAccountByAddress(address: String?): Single<Pair<EntityWallet?, Throwable?>> {
        return Single.create {
            try{
                if (habak == null || dbAccount == null || walletService == null) {
                    it.onSuccess(Pair(null, NullPointerException()))
                    return@create
                }

                if (!WalletUtil.isValidAddress(address)) {
                    it.onSuccess(Pair(null, InvalidAddressException()))
                    return@create
                }

                val account = dbAccount!!.accountDAO().getAccountByAddress(address!!).blockingGet()

                if (account == null){
                    it.onSuccess(Pair(null, DefaultAccountNotFoundException()))
                    return@create
                }
                val encryptData = account.encryptedData
                val walletSrc = habak!!.decrypt(EncryptedModel.readFromString(encryptData))
                if (walletSrc == null){
                    it.onSuccess(Pair(null, CorruptedHabakException()))
                    return@create
                }

                val entityWallet = EntityWallet.readFromString(walletSrc)
                if (entityWallet == null){
                    it.onSuccess(Pair(null, CorruptedHabakException()))
                    return@create
                }
                entityWallet.privateKey = ""
                entityWallet.dataSource = ""


                it.onSuccess(Pair(entityWallet, null))

            }catch(t: Throwable){
                it.onSuccess(Pair(null, t))
            }
        }
    }

    override fun removeAccount(address: String?): Single<Pair<EntityWallet?, Throwable?>> {
        return Single.create {
            try{
                if (habak == null || dbAccount == null || walletService == null) {
                    it.onSuccess(Pair(null, NullPointerException()))
                    return@create
                }

                val account = dbAccount!!.accountDAO().getAccountByAddress(address!!).blockingGet()

                if (account == null){
                    it.onSuccess(Pair(null, DefaultAccountNotFoundException()))
                    return@create
                }

                dbAccount!!.accountDAO().deleteAccount(account)
            }catch(t: Throwable){
                it.onSuccess(Pair(null, t))
            }
        }
    }

    override fun setAccountAsActive(address: String?): Single<Pair<EntityWallet?, Throwable?>> {

        return dbAccount!!.accountDAO().getAllAccounts().flatMap {list ->
            list.forEach {account ->
                account.isSelected = (address == account.address)
                dbAccount!!.accountDAO().updateAccount(account)
            }

            getAccountByAddress(address)
        }

        /*return Single.create {
            try{
                if (habak == null || dbAccount == null || walletService == null) {
                    it.onSuccess(Pair(null, NullPointerException()))
                    return@create
                }

                val allAccount = getAllAccount().blockingGet()


                val account = dbAccount!!.accountDAO().getAccountByAddress(address!!).blockingGet()

                if (account == null){
                    it.onSuccess(Pair(null, DefaultAccountNotFoundException()))
                    return@create
                }

                dbAccount!!.accountDAO().deleteAccount(account)
            }catch(t: Throwable){
                it.onSuccess(Pair(null, t))
            }
        }*/
    }
}