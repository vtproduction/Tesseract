package com.midsummer.tesseract.w3jl.components.tomoChain.masterNode

import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.listener.TransactionListener
import io.reactivex.Single
import java.math.BigInteger

/**
 * Created by cityme on 22,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
interface TomoValidatorService {

    fun createVoteData(candidate: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger) : Single<String>?
    fun createUnVoteData(candidate: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger) : Single<String>?
    fun createWithdrawData(blockNumber: BigInteger, index: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger) : Single<String>?
    fun createProposeData(coinBaseAddress: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger) : Single<String>?
    fun createResignData(coinBaseAddress: String, gasPrice: BigInteger, gasLimit: BigInteger) : Single<String>?


    fun vote(candidate: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger, callback: TransactionListener?)
    fun unVote(candidate: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger, callback: TransactionListener?)
    fun withdraw(blockNumber: BigInteger, index: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger, callback: TransactionListener?)
    fun propose(coinBaseAddress: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger, callback: TransactionListener?)
    fun resign(coinBaseAddress: String, gasPrice: BigInteger, gasLimit: BigInteger, callback: TransactionListener?)
}