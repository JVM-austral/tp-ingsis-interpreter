package lexer.rules

import token.TokenType

class StringAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val regex = """^(['"])[^'"]*\1$""".toRegex()
        return regex.matches(input)
    }

    override fun giveType(): TokenType {
        return TokenType.STRING_LITERAL
    }
}
