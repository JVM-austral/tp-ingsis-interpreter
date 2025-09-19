package lexer.newrules

import lexer.rules.TokenAnalyzer
import token.TokenType

class ReadInputAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val restrictedList = listOf("readInput")
        return restrictedList.contains(input)
    }

    override fun giveType(): TokenType = TokenType.IDENTIFIER
}
