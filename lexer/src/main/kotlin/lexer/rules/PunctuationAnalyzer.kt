package lexer.rules

import token.TokenType

class PunctuationAnalyzer : TokenAnalyzer {

    override fun analyze(input: String): Boolean {
        val punctuationList = listOf("}", "{", ";", "(", ")",":")
        return punctuationList.contains(input)
    }

    override fun giveType(): TokenType {
        return TokenType.PUNCTUATION;
    }
}