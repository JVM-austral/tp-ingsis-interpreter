package lexer.rules

import token.TokenType

class WhitespaceAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean = input == " "

    override fun giveType(): TokenType = TokenType.WHITESPACE
}
