package analyzers

import executors.FormatRulesExecutors
import executors.NoSpacesAfterEqualsExecutor
import token.Token

class NoSpacesAfterEqualsAnalyzer : FormatRulesAnalyzers {
    override fun analyze(
        exToken: Token,
        currentToken: Token,
        currenString: String,
    ): Boolean = currenString.last() == '=' && currentToken.type.name == "WHITESPACE"

    override fun giveExecutor(): FormatRulesExecutors = NoSpacesAfterEqualsExecutor()

    override fun stillNecessaryToAddToken(): Boolean = false
}
