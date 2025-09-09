package lexer.newrules

import lexer.rules.TokenAnalyzer
import token.TokenType

class ReadEnvAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val restrictedList = listOf("readEnv")
        return restrictedList.contains(input)
    }

    override fun giveType(): TokenType {
        return TokenType.IDENTIFIER
    }
}
