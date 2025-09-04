package lexer.newrules

import lexer.rules.TokenAnalyzer
import token.TokenType

class TabAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        return input == "\t"
    }

    override fun giveType(): TokenType {
        return TokenType.TAB
    }
}
