package newexecutors

import executors.FormatRulesExecutors
import token.Token

class IndentationExecutor(private val indentation: Int) : FormatRulesExecutors {
    override fun apply(exToken: Token, currentToken: Token, currentString: String): String {
        val braceLevel = bracesDifference(currentString)
        val tabAmount = when (currentToken.value) {
            "{" -> braceLevel * indentation
            "}" -> (braceLevel - 1).coerceAtLeast(0) * indentation
            else -> braceLevel * indentation
        }
        return putTabsAfterEnter(currentString, tabAmount)
    }

    private fun getTabs(tabAmount: Int): String = " ".repeat(tabAmount)

    private fun putTabsAfterEnter(currentString: String, tabAmount: Int): String {
        val lastEnterIndex = currentString.lastIndexOf('\n').takeIf { it != -1 } ?: 0
        val expectedTabs = getTabs(tabAmount)

        var i = lastEnterIndex + 1
        while (i < currentString.length && currentString[i] == ' ') {
            i++
        }

        val before = currentString.substring(0, lastEnterIndex + 1)
        val after = currentString.substring(i)

        return before + expectedTabs + after
    }

    private fun bracesDifference(code: String): Int {
        val lastEnterIndex = code.lastIndexOf('\n')
        if (lastEnterIndex == -1) return 0

        val codeUpToLastEnter = code.substring(0, lastEnterIndex)
        val openCount = codeUpToLastEnter.count { it == '{' }
        val closeCount = codeUpToLastEnter.count { it == '}' }

        return openCount - closeCount
    }
}
