import analyzers.FormatRulesAnalyzers
import token.Token

interface Formatter {

    fun format(tokens: List<Result<Token>>): String
    fun getAnalyzers(): List<FormatRulesAnalyzers>
}
