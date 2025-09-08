package lexer.newrules

import lexer.rules.TokenAnalyzer
import token.TokenType

class BooleanOperatorsAnalyzer : TokenAnalyzer {
    override fun analyze(input: String): Boolean {
        val restrictedList = listOf("==", "!=", ">", "<", ">=", "<=")
        return restrictedList.contains(input)
    }

    override fun giveType(): TokenType {
        return TokenType.BOOL_OPERATOR
    }
}
