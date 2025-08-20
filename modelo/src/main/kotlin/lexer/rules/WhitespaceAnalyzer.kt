package lexer.rules

import token.TokenType

class WhitespaceAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        return input == " "
    }

    override fun giveType(): TokenType {
        return TokenType.WHITESPACE;
    }
}