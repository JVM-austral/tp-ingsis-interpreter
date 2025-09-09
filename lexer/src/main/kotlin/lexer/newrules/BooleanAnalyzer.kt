package lexer.newrules

import lexer.rules.TokenAnalyzer
import token.TokenType

class BooleanAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val restrictedList = listOf("true", "false")
        return restrictedList.contains(input)
    }

    override fun giveType(): TokenType {
        return TokenType.BOOLEAN_LITERAL
    }
}
