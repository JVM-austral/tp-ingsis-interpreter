package analyzers

import token.Token

class NewLineAfterSemiColonAnalyzer : FormatRulesAnalyzers {
    override fun analyze(exToken: Token, currentToken: Token, currenString: String): Boolean {
        return exToken.value == ";" &&
            !currentToken.isOfType(token.TokenType.ENTER)
    }

    override fun giveExecutor(): executors.FormatRulesExecutors {
        return executors.NewLineAfterSemiColonExecutor()
    }
}
