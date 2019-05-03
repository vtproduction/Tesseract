package com.midsummer.tesseract.w3jl.constant.chain

/**
 * Created by NienLe on 2019-05-03,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
interface Chain {

    fun getEndpoint() : String
    fun getChainName() : String
    fun getChainId() : Int
    fun getExplorerURL() : String
    fun getCoinbaseUnit() : String
    fun getCoinbaseSymbol() : String
    fun getHDPath() : String
}