package newanalyzers

import analyzers.FormatRulesAnalyzers
import executors.FormatRulesExecutors
import newexecutors.IndentationExecutor
import token.TokenType

class IndentationAnalyzer(private val indentation: Int) : FormatRulesAnalyzers {
    override fun analyze(exToken: token.Token, currentToken: token.Token, currentString: String): Boolean {
        if (currentToken.type == TokenType.ENTER) {
            return false
        }
        return when (currentToken.value) {
            "{" -> hasToIndent(currentString, bracesDifference(currentString))
            "}" -> hasToIndent(currentString, bracesDifference(currentString) - 1)
            else -> hasToIndent(currentString, bracesDifference(currentString))
        }
    }

    override fun giveExecutor(): FormatRulesExecutors {
        return IndentationExecutor(indentation)
    }

    private fun hasToIndent(currentString: String, expectedBraceLevel: Int): Boolean {
        val lastEnterIndex = currentString.lastIndexOf('\n').takeIf { it != -1 } ?: 0

        var i = lastEnterIndex + 1
        while (i < currentString.length && currentString[i] == ' ') {
            i++
        }

        val actualSpaces = i - (lastEnterIndex + 1)
        val expectedSpaces = expectedBraceLevel * indentation

        return actualSpaces != expectedSpaces
    }

    private fun bracesDifference(code: String): Int {
        val lastEnterIndex = code.lastIndexOf('\n')
        if (lastEnterIndex == -1) return 0

        val codeUpToLastEnter = code.substring(0, lastEnterIndex)
        val openCount = codeUpToLastEnter.count { it == '{' }
        val closeCount = codeUpToLastEnter.count { it == '}' }

        return openCount - closeCount
    }

    override fun stillNecessaryToAddToken(): Boolean {
        return true
    }
}
