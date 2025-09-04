package analyzers

import executors.FormatRulesExecutors
import executors.SpaceAfterOperatorExecutor
import token.Token
import token.TokenType

class SpaceAfterOperatorAnalyzer : FormatRulesAnalyzers {
    override fun analyze(exToken: Token, currentToken: Token, currenString: String): Boolean {
        return !currentToken.isOfType(TokenType.WHITESPACE) &&
            (exToken.isOfType(TokenType.OPERATOR) || exToken.isOfType(TokenType.BOOLOPERATOR))
    }
    override fun giveExecutor(): FormatRulesExecutors {
        return SpaceAfterOperatorExecutor()
    }
}
