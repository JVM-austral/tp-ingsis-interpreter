package analyzers

import executors.FormatRulesExecutors
import executors.NoSpacesAfterEqualsExecutor
import token.Token

class NoSpacesAfterEqualsAnalyzer : FormatRulesAnalyzers {
    override fun analyze(exToken: Token, currentToken: Token, currenString: String): Boolean {
        return currenString.last() == '=' && currentToken.type.name == "WHITESPACE"
    }

    override fun giveExecutor(): FormatRulesExecutors {
        return NoSpacesAfterEqualsExecutor()
    }

    override fun stillNecessaryToAddToken(): Boolean {
        return false
    }
}
