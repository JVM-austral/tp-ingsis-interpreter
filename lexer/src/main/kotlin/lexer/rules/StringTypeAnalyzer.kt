package lexer.rules

import token.TokenType

class StringTypeAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        return input == "string"
    }

    override fun giveType(): TokenType {
        return TokenType.IDENTIFIER;
    }
}