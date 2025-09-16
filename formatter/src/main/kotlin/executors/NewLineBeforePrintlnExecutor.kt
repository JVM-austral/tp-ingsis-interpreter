package executors

import token.Token

class NewLineBeforePrintlnExecutor(private val amountOfNewLines: Int) : FormatRulesExecutors {
    override fun apply(exToken: Token, currentToken: Token, currentString: String): String {
        var newString = currentString
        val currentNewLines = countFinalNewLines(currentString)
        val difference = amountOfNewLines - currentNewLines

        when {
            difference > 0 -> {
                newString += "\n".repeat(difference)
            }
            difference < 0 -> {
                newString = newString.dropLast(-difference)
            }
        }

        return newString + currentToken.value
    }

    private fun countFinalNewLines(str: String): Int {
        var count = 0
        for (i in str.length - 1 downTo 0) {
            if (str[i] == '\n') count++ else break
        }
        return count
    }
}
