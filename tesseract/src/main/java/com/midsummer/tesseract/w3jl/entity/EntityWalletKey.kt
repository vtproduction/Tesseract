package com.midsummer.tesseract.w3jl.entity

import com.google.gson.Gson
import com.midsummer.tesseract.w3jl.constant.WalletSource

/**
 * Created by cityme on 29,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class EntityWalletKey {

    var address :  String = ""
    var privateKey : String = ""
    var publicKey : String = ""
    var mnemonics : String = ""
    var createdBy: Int = -1
    var metaData: String = ""


    fun writeToString() : String {
        return Gson().toJson(this)
    }

    companion object{
        fun readFromString(source: String?) : EntityWalletKey?{
            return try{
                Gson().fromJson<EntityWalletKey>(source, EntityWalletKey::class.java)
            }catch(t: Throwable){
                null
            }
        }
    }
}