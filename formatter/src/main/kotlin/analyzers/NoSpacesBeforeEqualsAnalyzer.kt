package analyzers

import executors.FormatRulesExecutors
import executors.NoSpacesBeforeEqualsExecutor
import token.Token

class NoSpacesBeforeEqualsAnalyzer : FormatRulesAnalyzers {
    override fun analyze(exToken: Token, currentToken: Token, currenString: String): Boolean {
        return currentToken.value == "=" && exToken.type.name == "WHITESPACE"
    }

    override fun giveExecutor(): FormatRulesExecutors {
        return NoSpacesBeforeEqualsExecutor()
    }

    override fun stillNecessaryToAddToken(): Boolean {
        return false
    }
}
