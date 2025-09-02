package lexer.newrules

import lexer.rules.TokenAnalyzer
import token.TokenType

class BooleanTypeAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val restrictedList = listOf("boolean")
        return restrictedList.contains(input)
    }

    override fun giveType(): TokenType {
        return TokenType.IDENTIFIER
    }
}
