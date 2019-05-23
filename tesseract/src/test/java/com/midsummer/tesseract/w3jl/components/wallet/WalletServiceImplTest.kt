package com.midsummer.tesseract.w3jl.components.wallet

import android.util.Log
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by cityme on 23,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class WalletServiceImplTest {

    private var walletServiceImpl : WalletServiceImpl? = null


    @Before
    fun setUp() {
        walletServiceImpl = WalletServiceImpl()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getWordList() {
        assertEquals(walletServiceImpl?.getWordList()?.size, 2048)
    }

    @Test
    fun generateMnemonics() {
        assertEquals(walletServiceImpl?.generateMnemonics()?.split(" ")?.size, 12)
    }

    @Test
    fun createWalletFromMnemonics() {
        val m = "arctic rotate inflict light mass artwork cat situate staff exotic clay random"
        assertEquals(walletServiceImpl?.createWalletFromMnemonics(m, "m/44'/889'/0'/0/0")?.blockingGet()?.address,"0x06605b28aab9835be75ca242a8ae58f2e15f2f45")
        assertNotEquals(walletServiceImpl?.createWalletFromMnemonics(m, "m/44'/889'/0'/0/1")?.blockingGet()?.address,"0x06605b28aab9835be75ca242a8ae58f2e15f2f45")
    }

    @Test
    fun createWalletFromPrivateKey() {
        val p = "fe514e9fa6e6f96e63640e80ba413ba0994bac81357fd7bab18b1302bf347750"
        assertEquals(walletServiceImpl?.createWalletFromPrivateKey(p)?.blockingGet()?.address,"0x06605b28aab9835be75ca242a8ae58f2e15f2f45")
        assertNotEquals(walletServiceImpl?.createWalletFromPrivateKey(p)?.blockingGet()?.address,"0x06605b28aab9835be75ca242a8ae58f2e15f2f46")
    }

    @Test
    fun createWalletFromAddress() {
    }
}