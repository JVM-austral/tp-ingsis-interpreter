package newanalyzers

import analyzers.FormatRulesAnalyzers
import executors.FormatRulesExecutors
import newexecutors.TabExecutor
import token.Token

class TabAnalyzer : FormatRulesAnalyzers {
    override fun analyze(exToken: Token, currentToken: Token, currenString: String): Boolean {
        return currentToken.value == "\t"
    }

    override fun giveExecutor(): FormatRulesExecutors {
        return TabExecutor()
    }
    override fun stillNecessaryToAddToken(): Boolean {
        return false
    }
}
