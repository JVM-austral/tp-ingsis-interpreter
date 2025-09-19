package analyzers

import executors.FormatRulesExecutors
import executors.SpaceBeforeColonExecutor
import token.Token
import token.TokenType

class SpaceBeforeColonAnalyzer : FormatRulesAnalyzers {
    override fun analyze(
        exToken: Token,
        currentToken: Token,
        currenString: String,
    ): Boolean =
        currentToken.isOfType(TokenType.PUNCTUATION) &&
            currentToken.value == ":" &&
            !exToken.isOfType(TokenType.WHITESPACE)

    override fun giveExecutor(): FormatRulesExecutors = SpaceBeforeColonExecutor()

    override fun stillNecessaryToAddToken(): Boolean = false
}
