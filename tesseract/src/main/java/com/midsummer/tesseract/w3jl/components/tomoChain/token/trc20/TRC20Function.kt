package com.midsummer.tesseract.w3jl.components.tomoChain.token.trc20

/**
 * Created by cityme on 23,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
enum class TRC20Function: TRC20FunctionType {

    TOTAL_SUPPLY{
        override fun functionName(): String {
            return "totalSupply"
        }

        override fun functionSignature(): String {
            return "0x18160ddd"
        }

        override fun argumentsCount(): Int {
            return 0
        }
    },
    BALANCE_OF{
        override fun functionName(): String {
            return "balanceOf"
        }

        override fun functionSignature(): String {
            return "0x70a08231"
        }

        override fun argumentsCount(): Int {
            return 1
        }
    },
    TRANSFER{
        override fun functionName(): String {
            return "transfer"
        }

        override fun functionSignature(): String {
            return "0xa9059cbb"
        }

        override fun argumentsCount(): Int {
            return 2
        }
    },
    TRANSFER_FROM{
        override fun functionName(): String {
            return "transferFrom"
        }

        override fun functionSignature(): String {
            return "0x23b872dd"
        }

        override fun argumentsCount(): Int {
            return 3
        }
    },
    APPROVE{
        override fun functionName(): String {
            return "approve"
        }

        override fun functionSignature(): String {
            return "0x095ea7b3"
        }

        override fun argumentsCount(): Int {
            return 2
        }
    },
    ALLOWANCE{
        override fun functionName(): String {
            return "allowance"
        }

        override fun functionSignature(): String {
            return "0xdd62ed3e"
        }

        override fun argumentsCount(): Int {
            return 2
        }
    },
    INVALID{
        override fun functionName(): String {
            return ""
        }

        override fun functionSignature(): String {
            return "invalid"
        }

        override fun argumentsCount(): Int {
            return 0
        }
    }
}