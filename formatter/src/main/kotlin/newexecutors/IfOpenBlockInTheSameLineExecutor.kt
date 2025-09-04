package newexecutors

import executors.FormatRulesExecutors
import token.Token

class IfOpenBlockInTheSameLineExecutor : FormatRulesExecutors {
    override fun apply(exToken: Token, currentToken: Token, currentString: String): String {
        val charactersToTake = amountOfEntersBefore(currentString)
        return currentString.dropLast(charactersToTake) + "{"
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
