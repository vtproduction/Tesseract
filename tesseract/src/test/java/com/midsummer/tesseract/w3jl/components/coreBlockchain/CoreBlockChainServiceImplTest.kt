package com.midsummer.tesseract.w3jl.components.coreBlockchain

import com.midsummer.tesseract.w3jl.entity.EntityWallet
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import java.math.BigInteger

/**
 * Created by cityme on 24,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class CoreBlockChainServiceImplTest {

    private var coreBlockChainServiceImpl : CoreBlockChainServiceImpl? = null

    @Before
    fun setUp() {
        val web3j = Web3j.build(HttpService("https://rpc.tomochain.com"))

        val wallet = EntityWallet()
        wallet.address = "0x6e7312d1028b70771bb9cdd9837442230a9349ca"
        coreBlockChainServiceImpl = CoreBlockChainServiceImpl(wallet,web3j)
    }

    @Test
    fun getAccountBalance() {
        assertEquals("get Account balance",BigInteger("9059497695991194751"), coreBlockChainServiceImpl?.getAccountBalance()?.blockingGet())
    }

    @Test
    fun getTransactionCount() {
        val wallet = EntityWallet()
        wallet.address = "0x6e7312d1028b70771bb9cdd9837442230a9349ca"
        assertEquals("get Account Transaction count",BigInteger("1284"), coreBlockChainServiceImpl?.getTransactionCount()?.blockingGet())
    }

    @Test
    fun transfer() {
    }


}