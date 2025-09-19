package newanalyzers

import analyzers.FormatRulesAnalyzers
import executors.FormatRulesExecutors
import newexecutors.IfOpenBlockUnderLineExecutor
import token.Token

class IfOpenBlockUnderLineAnalyzer : FormatRulesAnalyzers {
    override fun analyze(
        exToken: Token,
        currentToken: Token,
        currenString: String,
    ): Boolean = currentToken.value == "{" && amountOfEntersBefore(currenString) != 1

    override fun giveExecutor(): FormatRulesExecutors = IfOpenBlockUnderLineExecutor()

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

    override fun stillNecessaryToAddToken(): Boolean = false
}
