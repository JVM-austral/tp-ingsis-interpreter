package rules

import token.TokenType

class VariableAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val regex = "^[a-z]+$".toRegex()
        return regex.matches(input)
    }

    override fun giveType(): TokenType {
        return TokenType.IDENTIFIER;
    }
}