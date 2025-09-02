package executors

import token.Token

class NewLineBeforePrintlnExecutor(private val amountOfSpacesToTake: Int) : FormatRulesExecutors {
    override fun apply(exToken: Token, currentToken: Token, currentString: String): String {
        var newString = currentString
        var spacesToTake = countFinalNewLines(currentString) - amountOfSpacesToTake
        while (spacesToTake > 0 && newString.isNotEmpty() && newString.last() == '\n') {
            newString = newString.dropLast(1)
            spacesToTake--
        }
        return newString + currentToken.value
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
