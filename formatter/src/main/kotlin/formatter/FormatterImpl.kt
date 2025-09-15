package formatter

import analyzers.FormatRulesAnalyzers
import token.Token

class FormatterImpl(private val listOfAnalyzers: List<FormatRulesAnalyzers>) : Formatter {
    override fun format(tokens: List<Result<Token>>): String {
        var exToken = tokens[0].getOrNull() ?: return ""
        var formattedCode = ""

        if (exToken.value != " ") formattedCode += exToken.value

        for (i in 1 until tokens.size) {
            val curToken = tokens[i].getOrNull() ?: return ""
            formattedCode = formatIfNecessary(exToken, curToken, formattedCode)
            exToken = curToken
        }

        return formattedCode
    }

    private fun formatIfNecessary(exToken: Token, currentToken: Token, currentString: String): String {
        var formattedCode = currentString
        var hasChanged = false
        for (analyzer in listOfAnalyzers) {
            if (analyzer.analyze(exToken, currentToken, currentString)) {
                formattedCode = analyzer.giveExecutor().apply(exToken, currentToken, formattedCode)
                if (!analyzer.stillNecessaryToAddToken()) hasChanged = true
            }
        }
        if (!hasChanged) {
            return formattedCode + currentToken.value
        }
        return formattedCode
    }

    override fun getAnalyzers(): List<FormatRulesAnalyzers> {
        return listOfAnalyzers
    }
}
