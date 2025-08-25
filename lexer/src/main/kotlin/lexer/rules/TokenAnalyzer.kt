package lexer.rules

import token.TokenType

interface TokenAnalyzer {
    fun analyze(input: String): Boolean

    fun giveType(): TokenType
}
