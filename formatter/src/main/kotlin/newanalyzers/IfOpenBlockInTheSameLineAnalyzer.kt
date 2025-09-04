package newanalyzers

import analyzers.FormatRulesAnalyzers
import executors.FormatRulesExecutors
import newexecutors.IfOpenBlockInTheSameLineExecutor
import token.Token

class IfOpenBlockInTheSameLineAnalyzer : FormatRulesAnalyzers {
    override fun analyze(exToken: Token, currentToken: Token, currenString: String): Boolean {
        return currentToken.value == "{" && amountOfEntersBefore(currenString) > 0
    }

    override fun giveExecutor(): FormatRulesExecutors {
        return IfOpenBlockInTheSameLineExecutor()
    }

    private fun amountOfEntersBefore(string: String): Int {
        var i = string.length - 1
        var countNewlines = 0

        while (i >= 0 && string[i] == '\n') {
            countNewlines++
            i--
        }

        val before = string.substring(0, i + 1)

        val lastSemicolon = before.lastIndexOf(';')
        val lastIf = before.lastIndexOf("if")

        if (lastSemicolon > lastIf) return 0
        if (lastIf == -1) return 0

        return countNewlines
    }
}
