package newanalyzers

import analyzers.FormatRulesAnalyzers
import executors.FormatRulesExecutors
import newexecutors.IndentationExecutor
import token.Token

class IndentationAnalyzer : FormatRulesAnalyzers {
    override fun analyze(exToken: Token, currentToken: Token, currenString: String): Boolean {
        return currentToken.value == "\t"
    }

    override fun giveExecutor(): FormatRulesExecutors {
        return IndentationExecutor()
    }
}
