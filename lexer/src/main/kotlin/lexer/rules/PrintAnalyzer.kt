package lexer.rules

import token.TokenType

class PrintAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean = input == "println"

    override fun giveType(): TokenType = TokenType.IDENTIFIER
}
