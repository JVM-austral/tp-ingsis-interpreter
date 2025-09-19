package lexer.rules

import token.TokenType

class OperatorAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val operators = listOf("+", "-", "*", "/", "=")
        return operators.contains(input)
    }

    override fun giveType(): TokenType = TokenType.OPERATOR
}
