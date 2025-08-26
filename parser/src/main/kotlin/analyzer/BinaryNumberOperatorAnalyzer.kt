package analyzer

import BinaryNumberOperatorExecutor
import executor.StructureExecutor
import token.Token
import token.TokenType

class BinaryNumberOperatorAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        return isArithmeticOperation(tokens)
    }

    override fun getExecutor(): StructureExecutor {
        return BinaryNumberOperatorExecutor()
    }

    private fun isArithmeticOperation(tokens: List<Token>): Boolean {
        if (tokens.isEmpty()) return false

        val operators = setOf("+", "-", "*", "/")
        var balance = 0
        var expectOperand = true

        for (token in tokens) {
            when {
                expectOperand -> {
                    when (token.type) {
                        TokenType.NUMBER_LITERAL,
                        TokenType.IDENTIFIER,
                        -> { // ✅ ahora acepta variables también
                            expectOperand = false
                        }
                        TokenType.PUNCTUATION -> {
                            if (token.value == "(") {
                                balance++
                            } else {
                                return false
                            }
                        }
                        else -> return false
                    }
                }
                else -> {
                    when (token.type) {
                        TokenType.OPERATOR -> {
                            if (token.value in operators) {
                                expectOperand = true
                            } else {
                                return false
                            }
                        }
                        TokenType.PUNCTUATION -> {
                            if (token.value == ")") {
                                balance--
                                if (balance < 0) return false
                            } else {
                                return false
                            }
                        }
                        else -> return false
                    }
                }
            }
        }

        return !expectOperand && balance == 0
    }
}
