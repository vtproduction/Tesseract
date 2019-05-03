package com.midsummer.tesseract.w3jl.utils

import java.math.BigDecimal

/**
 * Created by NienLe on 2019-05-03,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
object ConvertUtil {
    fun fromWei(number: String, unit: Unit): BigDecimal {
        return fromWei(BigDecimal(number), unit)
    }

    fun fromWei(number: BigDecimal, unit: Unit): BigDecimal {
        return number.divide(unit.weiFactor)
    }

    fun toWei(number: String, unit: Unit): BigDecimal {
        return toWei(BigDecimal(number), unit)
    }

    fun toWei(number: BigDecimal, unit: Unit): BigDecimal {
        return number.multiply(unit.weiFactor)
    }

    enum class Unit(var name1: String, factor: Int) {
        WEI("wei", 0),
        KWEI("kwei", 3),
        MWEI("mwei", 6),
        GWEI("gwei", 9),
        SZABO("szabo", 12),
        FINNEY("finney", 15),
        ETHER("ether", 18),
        KETHER("kether", 21),
        METHER("mether", 24),
        GETHER("gether", 27);

        val weiFactor: BigDecimal = BigDecimal.TEN.pow(factor)

        override fun toString(): String {
            return name1
        }

        companion object {

            fun fromString(name1: String?): Unit {
                if (name1 != null) {
                    for (unit in values()) {
                        if (name1.equals(unit.name1, ignoreCase = true)) {
                            return unit
                        }
                    }
                }
                return valueOf(name1!!)
            }
        }
    }
}