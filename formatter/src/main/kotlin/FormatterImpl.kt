import analyzers.FormatRulesAnalyzers
import token.Token

class FormatterImpl(private val listOfAnalyzers: List<FormatRulesAnalyzers>) : Formatter {
    override fun format(tokens: List<Result<Token>>): String {
        if (tokens.isEmpty()) return ""
        var exToken = tokens[0].getOrNull() ?: return ""
        var formattedCode = ""

        if (exToken.value != " ") {
            formattedCode += exToken.value
        }

        for (i in 1 until tokens.size) {
            val curToken = tokens[i].getOrNull() ?: return ""
            formattedCode = formatIfNecessary(exToken, curToken, formattedCode)
            exToken = curToken
        }

        return formattedCode
    }

    private fun formatIfNecessary(exToken: Token, currentToken: Token, currentString: String): String {
        for (analyzer in listOfAnalyzers) {
            if (analyzer.analyze(exToken, currentToken, currentString)) {
                return analyzer.giveExecutor().apply(exToken, currentToken, currentString)
            }
        }
        return currentString + currentToken.value
    }
}
