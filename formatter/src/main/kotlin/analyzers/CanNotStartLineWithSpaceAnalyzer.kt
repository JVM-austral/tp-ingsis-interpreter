package analyzers

import token.Token

class CanNotStartLineWithSpaceAnalyzer : FormatRulesAnalyzers {
    override fun analyze(
        exToken: Token,
        currentToken: Token,
        currenString: String,
    ): Boolean = currenString.isNotEmpty() && currenString.last() == '\n' && currentToken.isOfType(token.TokenType.WHITESPACE)

    override fun giveExecutor(): executors.FormatRulesExecutors = executors.CanNotStartLineWithSpaceExecutor()

    override fun stillNecessaryToAddToken(): Boolean = false
}
