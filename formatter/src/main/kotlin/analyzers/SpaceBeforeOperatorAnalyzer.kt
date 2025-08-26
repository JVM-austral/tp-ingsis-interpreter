package analyzers

import executors.FormatRulesExecutors
import executors.SpaceBeforeOperatorExecutor
import token.Token
import token.TokenType

class SpaceBeforeOperatorAnalyzer : FormatRulesAnalyzers {
    override fun analyze(exToken: Token, currentToken: Token, currenString: String): Boolean {
        return currentToken.isOfType(TokenType.OPERATOR) &&
            !exToken.isOfType(TokenType.WHITESPACE)
    }

    override fun giveExecutor(): FormatRulesExecutors {
        return SpaceBeforeOperatorExecutor()
    }
}
