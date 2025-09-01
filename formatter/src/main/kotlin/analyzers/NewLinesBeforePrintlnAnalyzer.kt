package analyzers

import executors.NewLineBeforePrintlnExecutor

class NewLinesBeforePrintlnAnalyzer(private val amountOfLines: Int) : FormatRulesAnalyzers {
    override fun analyze(exToken: token.Token, currentToken: token.Token, currenString: String): Boolean {
        return currentToken.value == "println" && countFinalNewLines(currenString) > amountOfLines
    }

    override fun giveExecutor(): executors.FormatRulesExecutors {
        return NewLineBeforePrintlnExecutor(amountOfLines)
    }

    private fun countFinalNewLines(str: String): Int {
        var count = 0
        for (i in str.length - 1 downTo 0) {
            if (str[i] == '\n') {
                count++
            } else {
                break
            }
        }
        return count
    }
}
