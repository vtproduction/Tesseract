package com.midsummer.tesseract.w3jl.entity

import com.google.gson.Gson
import com.midsummer.tesseract.w3jl.constant.WalletSource
import java.util.*

/**
 * Created by NienLe on 2019-05-03,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class EntityWallet {


    var address :  String = ""
    var walletName : String = ""
    var lastBalance : String = ""
    var chainId : Int = -1
    var metadata: String = ""
    var createdBy : Int = WalletSource.UNKNOWN
    var createAt : Long = 0L


    fun writeToString() : String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "EntityWallet(address='$address', walletName='$walletName', lastBalance='$lastBalance', chainId=$chainId, metadata='$metadata', createdBy=$createdBy, createAt=$createAt)"
    }


    companion object{
        fun readFromString(source: String?) : EntityWallet?{
            return try{
                Gson().fromJson<EntityWallet>(source, EntityWallet::class.java)
            }catch(t: Throwable){
                null
            }
        }


        fun readFromKey(src: EntityWalletKey) : EntityWallet?{
            return try {
                val w = EntityWallet()
                w.address = src.address
                w.walletName = ""
                w.lastBalance = "0"
                w.chainId = -1
                w.createdBy = src.createdBy
                w.createAt = Calendar.getInstance().timeInMillis
                w
            }catch (t: Throwable){
                null
            }
        }
    }
}