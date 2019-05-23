package com.midsummer.tesseract.w3jl.components.tomoChain.masterNode

import com.midsummer.tesseract.w3jl.entity.EntityWallet
import com.midsummer.tesseract.w3jl.listener.TransactionListener
import java.math.BigInteger

/**
 * Created by cityme on 22,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
interface TomoValidatorService {

    fun createVoteData(account: EntityWallet?, candidate: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger) : String
    fun createUnVoteData(account: EntityWallet?, candidate: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger) : String
    fun createWithdrawData(account: EntityWallet?, blockNumber: BigInteger, index: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger) : String
    fun createProposeData(account: EntityWallet?, coinBaseAddress: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger) : String
    fun createResignData(account: EntityWallet?, coinBaseAddress: String, gasPrice: BigInteger, gasLimit: BigInteger) : String


    fun vote(account: EntityWallet?, candidate: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger, callback: TransactionListener?)
    fun unVote(account: EntityWallet?, candidate: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger, callback: TransactionListener?)
    fun withdraw(account: EntityWallet?, blockNumber: BigInteger, index: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger, callback: TransactionListener?)
    fun propose(account: EntityWallet?, coinBaseAddress: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger, callback: TransactionListener?)
    fun resign(account: EntityWallet?, coinBaseAddress: String, gasPrice: BigInteger, gasLimit: BigInteger, callback: TransactionListener?)
}