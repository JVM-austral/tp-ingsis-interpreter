package lexer.rules

import token.TokenType

class OperatorAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val operators = listOf("+", "-", "x", "/", "=")
        return operators.contains(input)
    }

    override fun giveType(): TokenType {
        return TokenType.OPERATOR
    }
}
