package analyzers

import executors.FormatRulesExecutors
import executors.SpaceAfterEqualsExecutor
import token.Token
import token.TokenType

class SpaceAfterEqualsAnalyzer : FormatRulesAnalyzers {
    override fun analyze(
        exToken: Token,
        currentToken: Token,
        currenString: String,
    ): Boolean =
        !currentToken.isOfType(TokenType.WHITESPACE) &&
            exToken.value == "=" &&
            exToken.isOfType(TokenType.OPERATOR)

    override fun giveExecutor(): FormatRulesExecutors = SpaceAfterEqualsExecutor()

    override fun stillNecessaryToAddToken(): Boolean = false
}
