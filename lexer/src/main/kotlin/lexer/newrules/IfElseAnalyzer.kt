package lexer.newrules

import lexer.rules.TokenAnalyzer
import token.TokenType

class IfElseAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val restrictedList = listOf("if", "else")
        return restrictedList.contains(input)
    }

    override fun giveType(): TokenType {
        return TokenType.CONDITIONAL
    }
}
