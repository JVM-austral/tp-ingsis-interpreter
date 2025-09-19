package analyzers

import token.Token

class NewLineAfterSemiColonAnalyzer : FormatRulesAnalyzers {
    override fun analyze(
        exToken: Token,
        currentToken: Token,
        currenString: String,
    ): Boolean =
        exToken.value == ";" &&
            !currentToken.isOfType(token.TokenType.ENTER)

    override fun giveExecutor(): executors.FormatRulesExecutors = executors.NewLineAfterSemiColonExecutor()

    override fun stillNecessaryToAddToken(): Boolean = false
}
