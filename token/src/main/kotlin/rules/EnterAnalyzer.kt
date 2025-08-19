package rules

import token.TokenType

class EnterAnalyzer: TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val restrictedList= listOf("\n")
        return restrictedList.contains(input)
    }

    override fun giveType(): TokenType {
        return TokenType.ENTER;
    }
}