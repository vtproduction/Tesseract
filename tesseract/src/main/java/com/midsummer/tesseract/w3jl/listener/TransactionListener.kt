package com.midsummer.tesseract.w3jl.listener

/**
 * Created by cityme on 22,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
interface TransactionListener {
    fun onTransactionCreated(txId: String)
    fun onTransactionComplete(txId: String, status: String)
    fun onTransactionError(e: Exception)
}