package lexer.rules

import token.TokenType

class PrintAnalyzer : TokenAnalyzer {

    override fun analyze(input: String): Boolean {
        return input == "println"
    }

    override fun giveType(): TokenType {
        return TokenType.IDENTIFIER
    }
}
