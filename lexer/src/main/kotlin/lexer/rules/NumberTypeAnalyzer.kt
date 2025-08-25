package lexer.rules

import token.TokenType

class NumberTypeAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        return input == "number"
    }

    override fun giveType(): TokenType {
        return TokenType.IDENTIFIER
    }
}
