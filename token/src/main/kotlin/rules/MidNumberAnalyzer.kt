package rules

import token.TokenType

class MidNumberAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val floatRegex = """^\d+(\.)?$""".toRegex()

        return floatRegex.matches(input)
    }

    override fun giveType(): TokenType {
        return TokenType.UNKNOWN;
    }
}