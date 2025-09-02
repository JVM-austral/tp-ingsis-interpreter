package lexer.newrules

import lexer.rules.TokenAnalyzer
import token.TokenType

class ConstAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val restrictedList = listOf("const")
        return restrictedList.contains(input)
    }

    override fun giveType(): TokenType {
        return TokenType.KEYWORD
    }
}
