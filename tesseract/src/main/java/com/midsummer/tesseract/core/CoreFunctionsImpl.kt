package com.midsummer.tesseract.core

import android.annotation.SuppressLint
import android.util.Log
import com.midsummer.tesseract.common.LogTag
import com.midsummer.tesseract.common.exception.*
import com.midsummer.tesseract.habak.EncryptedModel
import com.midsummer.tesseract.habak.cryptography.Habak
import com.midsummer.tesseract.room.entity.account.DatabaseAccount
import com.midsummer.tesseract.room.entity.account.EntityAccount
import com.midsummer.tesseract.w3jl.components.wallet.WalletService
import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.utils.WalletUtil
import io.reactivex.Single

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


    @SuppressLint("CheckResult")
    override fun createAccountFromMnemonics(mnemonics: String?, hdPath: String): Single<Pair<String?, Throwable?>> {
        return Single.create {
            try{
                if (habak == null || dbAccount == null || walletService == null) {
                    it.onSuccess(Pair(null, NullPointerException("1")))
                    return@create
                }

                if (!WalletUtil.isValidMnemonics(mnemonics, false)){
                    it.onSuccess(Pair(null, InvalidMnemonicException()))
                    return@create
                }
                val entityWalletKey = walletService?.createWalletFromMnemonics(mnemonics!!, hdPath)?.blockingGet()

                if (entityWalletKey == null) {
                    it.onSuccess(Pair(null, NullPointerException("22")))
                    return@create
                }

                val entityWallet = EntityWallet.readFromKey(entityWalletKey)
                dbAccount!!.accountDAO().getAccountByAddress(entityWalletKey.address)
                    .subscribe(
                        { _ ->
                            it.onSuccess(Pair(null, AccountAlreadyExistedException()))
                            return@subscribe
                        },
                        { _ ->
                            val encryptedData = habak!!.encrypt(entityWalletKey.writeToString())
                            val entityAccount = EntityAccount(
                                entityWalletKey.address, false, encryptedData.writeToString(), entityWallet!!.createAt, entityWallet.createAt,
                                metadata = entityWallet.writeToString())

                            dbAccount!!.accountDAO().addAccount(entityAccount)
                                .subscribe()
                            it.onSuccess(Pair(entityWallet.address, null))
                        }
                    )
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
                val entityWalletKey = walletService?.createWalletFromPrivateKey(privateKey!!)?.blockingGet()

                if (entityWalletKey == null) {
                    it.onSuccess(Pair(null, NullPointerException("22")))
                    return@create
                }

                val entityWallet = EntityWallet.readFromKey(entityWalletKey)
                dbAccount!!.accountDAO().getAccountByAddress(entityWalletKey.address)
                    .subscribe(
                        { _ ->
                            it.onSuccess(Pair(null, AccountAlreadyExistedException()))
                            return@subscribe
                        },
                        { _ ->
                            val encryptedData = habak!!.encrypt(entityWalletKey.writeToString())
                            val entityAccount = EntityAccount(
                                entityWalletKey.address, false, encryptedData.writeToString(), entityWallet!!.createAt, entityWallet.createAt,
                                metadata = entityWallet.writeToString())

                            dbAccount!!.accountDAO().addAccount(entityAccount)
                                .subscribe()
                            it.onSuccess(Pair(entityWallet.address, null))
                        }
                    )

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
                val entityWalletKey = walletService?.createWalletFromAddress(address!!)?.blockingGet()

                if (entityWalletKey == null) {
                    it.onSuccess(Pair(null, NullPointerException("22")))
                    return@create
                }

                val entityWallet = EntityWallet.readFromKey(entityWalletKey)
                dbAccount!!.accountDAO().getAccountByAddress(entityWalletKey.address)
                    .subscribe(
                        { _ ->
                            it.onSuccess(Pair(null, AccountAlreadyExistedException()))
                            return@subscribe
                        },
                        { _ ->
                            val encryptedData = habak!!.encrypt(entityWalletKey.writeToString())
                            val entityAccount = EntityAccount(
                                entityWalletKey.address, false, encryptedData.writeToString(), entityWallet!!.createAt, entityWallet.createAt,
                                metadata = entityWallet.writeToString())

                            dbAccount!!.accountDAO().addAccount(entityAccount)
                                .subscribe()
                            it.onSuccess(Pair(entityWallet.address, null))
                        }
                    )

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

                val entityWallet = EntityWallet.readFromString(activeAccount.metadata)
                if (entityWallet == null){
                    it.onSuccess(Pair(null, CorruptedHabakException()))
                    return@create
                }

                it.onSuccess(Pair(entityWallet, null))

            }catch(t: Throwable){
                it.onSuccess(Pair(null, t))
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun getAllAccount(): Single<Pair<MutableList<EntityWallet>?, Throwable?>> {
        return Single.create {
            try{
                if (habak == null || dbAccount == null || walletService == null) {
                    it.onSuccess(Pair(null, NullPointerException()))
                    return@create
                }

                dbAccount!!.accountDAO().getAllAccounts()
                    .subscribe(
                        {r ->
                            val list : MutableList<EntityWallet> = arrayListOf()
                            r.forEach { a ->
                                val entityWallet = EntityWallet.readFromString(a.metadata)
                                if (entityWallet != null){
                                    entityWallet.metadata = if (a.isSelected) "1" else "0"
                                    list.add(entityWallet)
                                }
                            }

                            it.onSuccess(Pair(list, null))
                        },
                        {t ->
                            Log.e(LogTag.TAG_W3JL,"CoreFunctionsImpl > getAllAccount: ${t.localizedMessage}")
                            it.onSuccess(Pair(null, t))
                        }
                    )

            }catch(t: Throwable){
                it.onSuccess(Pair(null, t))
            }
        }
    }

    @SuppressLint("CheckResult")
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

                dbAccount!!.accountDAO().getAccountByAddress(address!!)
                    .subscribe(
                        { account ->
                            val entityWallet = EntityWallet.readFromString(account?.metadata)
                            it.onSuccess(Pair(entityWallet, null))
                        },
                        { _ ->
                            it.onSuccess(Pair(null, DefaultAccountNotFoundException()))
                        }
                    )
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

                dbAccount!!.accountDAO().getAccountByAddress(address!!)
                    .subscribe(
                        { account ->
                            dbAccount!!.accountDAO().deleteAccount(account!!).subscribe()
                            val entityWallet = EntityWallet.readFromString(account.metadata)
                            it.onSuccess(Pair(entityWallet, null))
                        },
                        { _ ->
                            it.onSuccess(Pair(null, DefaultAccountNotFoundException()))
                        }
                    )
            }catch(t: Throwable){
                it.onSuccess(Pair(null, t))
            }
        }
    }

    override fun setAccountAsActive(address: String?): Single<Pair<EntityWallet?, Throwable?>> {

        return dbAccount!!.accountDAO().getAllAccounts().flatMap {list ->
            list.forEach {account ->
                account.isSelected = (address == account.address)
                dbAccount!!.accountDAO().updateAccount(account).subscribe()
            }

            getAccountByAddress(address)
        }

    }


    override fun setAccountName(address: String?, name: String): Single<Pair<EntityWallet?, Throwable?>> {
        return dbAccount!!.accountDAO().getAccountByAddress(address!!).flatMap {

            val entityWallet = EntityWallet.readFromString(it.metadata)
            entityWallet?.walletName = name
            it.metadata = entityWallet?.writeToString()!!
            dbAccount!!.accountDAO().updateAccount(it).subscribe()

            getAccountByAddress(address)
        }
    }
}