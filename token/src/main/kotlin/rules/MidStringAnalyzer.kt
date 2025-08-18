package rules

import token.TokenType

class MidStringAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val regex = """^(['"])[^'"]*$""".toRegex()
        return regex.matches(input);
    }

    override fun giveType(): TokenType {
        return TokenType.UNKNOWN;
    }

}