package lexer.rules

import token.TokenType

class VariableAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val regex = "^[a-zA-Z_]+$".toRegex()
        return regex.matches(input)
    }

    override fun giveType(): TokenType = TokenType.IDENTIFIER
}
