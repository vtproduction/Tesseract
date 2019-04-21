package com.midsummer.tesseractSample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import com.midsummer.tesseract.room.entity.DatabaseAccount
import com.midsummer.tesseract.room.entity.EntityAccount
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var mDB : DatabaseAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        mDB = DatabaseAccount.getInstance(this)

        btnAddTrue.setOnClickListener { add(true) }
        btnAddFalse.setOnClickListener { add(false) }
        btnViewAll.setOnClickListener { viewAll() }
        btnViewActive.setOnClickListener { viewActive() }
        btnDeleteAll.setOnClickListener { delete() }
    }


    private fun add(active: Boolean){
        val account = EntityAccount("${Calendar.getInstance().timeInMillis}",active,"yeah",0,0,">>")
        mDB?.accountDAO()?.addAccount(account)
            ?.subscribeOn(Schedulers.io())
            ?.doOnSuccess {
                Log.d("TESSERACT-TAG", "add success: $it")
            }
            ?.doOnError {
                Log.d("TESSERACT-TAG", "add error: ${it.localizedMessage}")
            }
            ?.subscribe()
    }


    private fun viewActive(){

        mDB?.accountDAO()?.getActiveAccount()
            ?.subscribeOn(Schedulers.io())
            ?.doOnSuccess {
                Log.d("TESSERACT-TAG", "view: $it")
            }
            ?.doOnError {
                Log.d("TESSERACT-TAG", "view error: ${it.localizedMessage}")
            }
            ?.subscribe()

    }
    
    private fun delete(){
        mDB?.accountDAO()?.kaboom()
            ?.subscribeOn(Schedulers.io())
            ?.doOnSuccess {
                Log.d("TESSERACT-TAG", "delete: $it")
            }
            ?.doOnError {
                Log.d("TESSERACT-TAG", "delete error: ${it.localizedMessage}")
            }
            ?.subscribe()
    }

    private fun viewAll(){

        mDB?.accountDAO()?.getAllAccounts()
            ?.subscribeOn(Schedulers.io())
            ?.doOnSuccess {
                it.forEach {record ->
                    Log.d("TESSERACT-TAG", "view: $record")
                }
            }
            ?.doOnError {
                Log.d("TESSERACT-TAG", "view error: ${it.localizedMessage}")
            }
            ?.subscribe()

    }
}
