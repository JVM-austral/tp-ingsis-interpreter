package lexer.rules

import token.TokenType

class KeywordAnalyzer: TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val restrictedList= listOf("let")
        return restrictedList.contains(input)
    }

    override fun giveType(): TokenType {
        return TokenType.KEYWORD;
    }
}