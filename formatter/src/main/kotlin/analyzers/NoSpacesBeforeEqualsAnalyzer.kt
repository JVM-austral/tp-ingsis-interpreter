package analyzers

import executors.FormatRulesExecutors
import executors.NoSpacesBeforeEqualsExecutor
import token.Token

class NoSpacesBeforeEqualsAnalyzer : FormatRulesAnalyzers {
    override fun analyze(
        exToken: Token,
        currentToken: Token,
        currenString: String,
    ): Boolean = currentToken.value == "=" && exToken.type.name == "WHITESPACE"

    override fun giveExecutor(): FormatRulesExecutors = NoSpacesBeforeEqualsExecutor()

    override fun stillNecessaryToAddToken(): Boolean = false
}
