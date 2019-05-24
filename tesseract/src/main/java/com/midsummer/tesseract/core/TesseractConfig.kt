package com.midsummer.tesseract.core

import com.midsummer.tesseract.w3jl.constant.chain.Chain

/**
 * Created by cityme on 24,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
abstract class TesseractConfig {

    abstract fun chain() : Chain
    abstract fun habakAlias() :  String
    abstract fun roomHelperSalt() : String
}