package lexer.rules

import token.TokenType

class NumberAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val intRegex = """^\d+$""".toRegex()
        val floatRegex = """^\d+(\.\d+)?$""".toRegex()

        return intRegex.matches(input) || floatRegex.matches(input)
    }

    override fun giveType(): TokenType {
        return TokenType.NUMBER_LITERAL;
    }
}