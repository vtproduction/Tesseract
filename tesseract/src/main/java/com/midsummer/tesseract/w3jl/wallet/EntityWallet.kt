package com.midsummer.tesseract.w3jl.wallet

import com.midsummer.tesseract.w3jl.constant.WalletSource

/**
 * Created by NienLe on 2019-05-03,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class EntityWallet {


    var address :  String = ""
    var privateKey : String = ""
    var publicKey : String = ""
    var dataSource : String = ""
    var metadata: String = ""
    var walletName : String  = ""
    var chainId : Int = -1
    var createdBy : Int = WalletSource.UNKNOWN
    var createAt : Long = 0L

}