package lexer.rules

import token.TokenType

class NumberTypeAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean = input == "number"

    override fun giveType(): TokenType = TokenType.IDENTIFIER
}
