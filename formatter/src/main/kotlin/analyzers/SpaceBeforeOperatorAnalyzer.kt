package analyzers

import executors.FormatRulesExecutors
import executors.SpaceBeforeOperatorExecutor
import token.Token
import token.TokenType

class SpaceBeforeOperatorAnalyzer : FormatRulesAnalyzers {
    override fun analyze(exToken: Token, currentToken: Token, currenString: String): Boolean {
        return (currentToken.isOfType(TokenType.OPERATOR) || currentToken.isOfType(TokenType.BOOLOPERATOR)) &&
            !exToken.isOfType(TokenType.WHITESPACE) && currentToken.value != "="
    }

    override fun giveExecutor(): FormatRulesExecutors {
        return SpaceBeforeOperatorExecutor()
    }
    override fun stillNecessaryToAddToken(): Boolean {
        return false
    }
}
