import analyzers.FormatRulesAnalyzers
import token.Token

class FormatterImpl(private val listOfAnalyzers: List<FormatRulesAnalyzers>, private val indentationSize: Int) : Formatter {
    override fun format(tokens: List<Result<Token>>): String {
        var exToken = tokens[0].getOrNull() ?: return ""
        var formattedCode = ""
        var tabAmount = 0

        if (exToken.value != " ") formattedCode += exToken.value

        for (i in 1 until tokens.size) {
            val curToken = tokens[i].getOrNull() ?: return ""
            tabAmount = updateTabAmount(curToken, exToken, tabAmount)
            formattedCode = formatIfNecessary(exToken, curToken, formattedCode, tabAmount * indentationSize)
            exToken = curToken
        }

        return formattedCode
    }

    private fun formatIfNecessary(exToken: Token, currentToken: Token, currentString: String, tabAmount: Int): String {
        var formattedCode = ""
        for (analyzer in listOfAnalyzers) {
            if (analyzer.analyze(exToken, currentToken, currentString)) {
                return putTabsAfterEnter(analyzer.giveExecutor().apply(exToken, currentToken, currentString), tabAmount)
            }
        }
        return putTabsAfterEnter(currentString + currentToken.value, tabAmount)
    }

    private fun updateTabAmount(currentToken: Token, exToken: Token, tabAmount: Int): Int {
        if (exToken.value == "{") {
            return tabAmount + 1
        } else if (currentToken.value == "}") {
            if (tabAmount == 0) {
                return 0
            }
            return tabAmount - 1
        }
        return tabAmount
    }
    private fun getTabs(tabAmount: Int): String {
        var tabs = ""

        for (i in 0 until tabAmount) {
            tabs += "\t"
        }
        return tabs
    }

    private fun putTabsAfterEnter(currentString: String, tabAmount: Int): String {
        val lastEnterIndex = currentString.lastIndexOf('\n')
        if (lastEnterIndex == -1) return currentString

        val expectedTabs = getTabs(tabAmount)

        var i = lastEnterIndex + 1
        while (i < currentString.length && currentString[i] == '\t') {
            i++
        }

        val before = currentString.substring(0, lastEnterIndex + 1)

        val after = currentString.substring(i)

        return before + expectedTabs + after
    }

    override fun getAnalyzers(): List<FormatRulesAnalyzers> {
        return listOfAnalyzers
    }
}
