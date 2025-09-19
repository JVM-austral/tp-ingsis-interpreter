package lexer.rules

import token.TokenType

class StringTypeAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean = input == "string"

    override fun giveType(): TokenType = TokenType.IDENTIFIER
}
