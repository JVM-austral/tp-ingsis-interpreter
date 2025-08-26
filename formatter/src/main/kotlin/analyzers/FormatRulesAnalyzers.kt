package analyzers

import executors.FormatRulesExecutors
import token.Token

interface FormatRulesAnalyzers {
    fun analyze(exToken: Token, currentToken: Token, currenString: String): Boolean
    fun giveExecutor(): FormatRulesExecutors
}
