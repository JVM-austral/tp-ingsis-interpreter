package analyzer

import ConditionExecutor
import executor.StructureExecutor
import token.Token
import token.TokenType

class ConditionAnalyzer : StructureAnalyzer {
    override fun analyzeStructure(tokens: List<Token>): Boolean {
        return isBooleanExpression(tokens)
    }

    override fun getExecutor(): StructureExecutor {
        return ConditionExecutor()
    }

    private fun isBooleanExpression(tokens: List<Token>): Boolean {
        if (tokens.isEmpty()) return false

        val comparisonOperators = setOf("==", "!=", ">", "<", ">=", "<=")
        val logicalOperators = setOf("&&", "||")
        val arithmeticOperators = setOf("+", "-", "*", "/")

        var balance = 0
        var expectOperand = true

        for (token in tokens) {
            when {
                expectOperand -> {
                    when (token.type) {
                        TokenType.NUMBER_LITERAL,
                        TokenType.IDENTIFIER,
                        TokenType.BOOLEAN_LITERAL,
                        -> {
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
                            when (token.value) {
                                in comparisonOperators,
                                in logicalOperators,
                                in arithmeticOperators,
                                -> expectOperand = true
                                else -> return false
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
