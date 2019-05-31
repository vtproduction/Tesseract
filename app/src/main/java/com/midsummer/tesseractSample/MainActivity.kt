package com.midsummer.tesseractSample

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.midsummer.tesseract.common.exception.AccountAlreadyExistedException
import com.midsummer.tesseract.core.CoreFunctions
import com.midsummer.tesseract.core.Tesseract
import com.midsummer.tesseract.room.entity.account.DatabaseAccount
import com.midsummer.tesseract.room.entity.account.EntityAccount
import com.midsummer.tesseract.w3jl.components.coreBlockchain.BlockChainService
import com.midsummer.tesseract.w3jl.utils.WalletUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var coreFunction : CoreFunctions? = null
    private var blockChainService: BlockChainService? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        coreFunction = Tesseract.getInstance()?.getCoreFunctions()
        blockChainService = Tesseract.getInstance()?.getCoreBlockChainService()

        btnAddTrue.setOnClickListener { add() }
        btnAddFalse.setOnClickListener { add() }
        btnViewAll.setOnClickListener { viewAll() }
        btnViewActive.setOnClickListener { viewActive() }
        btnDeleteAll.setOnClickListener { delete() }
        btnSetActive.setOnClickListener{setActive()}
        btnChangeName.setOnClickListener { changeWalletName() }
    }

    @SuppressLint("CheckResult")
    private fun changeWalletName() {
        blockChainService?.getAccountBalance("0x6e7312d1028b70771bb9cdd9837442230a9349ca", null)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe {
                LogUtil.d("MainActivity > setAccountName: on subscribe")
            }
            ?.subscribe (
                {
                    LogUtil.d("MainActivity > changeWalletName: $it")
                },
                {
                    LogUtil.e("MainActivity > setAccountName fail: ${it.localizedMessage}")
                }
            )
        /*coreFunction?.setAccountName("0x9393905115ac7fde290381a0fecf5751250f2d38","NAME")
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe {
                LogUtil.d("MainActivity > setAccountName: on subscribe")
            }
            ?.subscribe (
                {
                    LogUtil.d("MainActivity > setAccountName success: ${it.first}")
                    if (it.second != null){
                        if (it.second is AccountAlreadyExistedException){
                            LogUtil.e("MainActivity > setAccountName: AccountAlreadyExistedException")
                        }
                    }
                },
                {
                    LogUtil.e("MainActivity > setAccountName fail: ${it.localizedMessage}")
                }
            )*/



    }

    @SuppressLint("CheckResult")
    private fun setActive() {
        coreFunction?.setAccountAsActive("0x9393905115ac7fde290381a0fecf5751250f2d38")
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe {
                LogUtil.d("MainActivity > setAccountAsActive: on subscribe")
            }
            ?.subscribe (
                {
                    LogUtil.d("MainActivity > setAccountAsActive success: ${it.first}")
                    if (it.second != null){
                        if (it.second is AccountAlreadyExistedException){
                            LogUtil.e("MainActivity > setAccountAsActive: AccountAlreadyExistedException")
                        }
                    }
                },
                {
                    LogUtil.e("MainActivity > setAccountAsActive fail: ${it.localizedMessage}")
                }
            )
    }


    @SuppressLint("CheckResult")
    private fun add(){
        val mnemonics = WalletUtil.generateMnemonics()
        coreFunction?.createAccountFromMnemonics(mnemonics)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe {
                LogUtil.d("MainActivity > add: on subscribe")
            }
            ?.subscribe (
                {
                    LogUtil.d("MainActivity > add success: ${it.first}")
                    if (it.second != null){
                        if (it.second is AccountAlreadyExistedException){
                            LogUtil.e("MainActivity > add: AccountAlreadyExistedException")
                        }
                    }
                },
                {
                    LogUtil.e("MainActivity > add fail: ${it.localizedMessage}")
                }
            )
    }


    @SuppressLint("CheckResult")
    private fun viewActive(){
        coreFunction?.getActiveAccount()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe {
                LogUtil.d("MainActivity > viewActive: on subscribe")
            }
            ?.subscribe (
                {
                    LogUtil.d("MainActivity > viewActive success: ${it.first}")
                    if (it.second != null){
                        if (it.second is AccountAlreadyExistedException){
                            LogUtil.e("MainActivity > viewActive: AccountAlreadyExistedException")
                        }
                    }
                },
                {
                    LogUtil.e("MainActivity > viewActive fail: ${it.localizedMessage}")
                }
            )
    }
    
    @SuppressLint("CheckResult")
    private fun delete(){
        coreFunction?.removeAccount("0x9393905115ac7fde290381a0fecf5751250f2d38")
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe {
                LogUtil.d("MainActivity > delete: on subscribe")
            }
            ?.subscribe (
                {
                    LogUtil.d("MainActivity > delete success: ${it.first}")
                    if (it.second != null){
                        if (it.second is AccountAlreadyExistedException){
                            LogUtil.e("MainActivity > delete: AccountAlreadyExistedException")
                        }
                    }
                },
                {
                    LogUtil.e("MainActivity > delete fail: ${it.localizedMessage}")
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun viewAll(){
        coreFunction?.getAllAccount()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe {
                LogUtil.d("MainActivity > viewAll: on subscribe")
            }
            ?.subscribe (
                {
                    LogUtil.d("MainActivity > viewAll success: ${it.first?.size}")
                    it.first?.forEach { wallet -> 
                        LogUtil.d("MainActivity > viewAll: $wallet")
                    }
                    
                },
                {
                    LogUtil.e("MainActivity > getAllAccount fail: ${it.localizedMessage}")
                }
            )

    }
}
