package analyzers

import executors.FormatRulesExecutors
import executors.SpaceBeforeEqualsExecutor
import token.Token
import token.TokenType

class SpaceBeforeEqualsAnalyzer : FormatRulesAnalyzers {
    override fun analyze(
        exToken: Token,
        currentToken: Token,
        currenString: String,
    ): Boolean =
        currentToken.isOfType(TokenType.OPERATOR) &&
            currentToken.value == "=" &&
            !exToken.isOfType(TokenType.WHITESPACE)

    override fun giveExecutor(): FormatRulesExecutors = SpaceBeforeEqualsExecutor()

    override fun stillNecessaryToAddToken(): Boolean = false
}
