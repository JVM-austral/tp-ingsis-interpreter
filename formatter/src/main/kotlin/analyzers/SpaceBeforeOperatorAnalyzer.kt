package analyzers

import executors.FormatRulesExecutors
import executors.SpaceBeforeOperatorExecutor
import token.Token
import token.TokenType

class SpaceBeforeOperatorAnalyzer : FormatRulesAnalyzers {
    override fun analyze(
        exToken: Token,
        currentToken: Token,
        currenString: String,
    ): Boolean =
        (currentToken.isOfType(TokenType.OPERATOR) || currentToken.isOfType(TokenType.BOOL_OPERATOR)) &&
            !exToken.isOfType(TokenType.WHITESPACE) &&
            currentToken.value != "="

    override fun giveExecutor(): FormatRulesExecutors = SpaceBeforeOperatorExecutor()

    override fun stillNecessaryToAddToken(): Boolean = false
}
