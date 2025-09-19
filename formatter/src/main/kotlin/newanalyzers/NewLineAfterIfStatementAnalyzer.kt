package newanalyzers

import analyzers.FormatRulesAnalyzers
import executors.FormatRulesExecutors
import newexecutors.NewLineAfterIfStatementExecutor
import token.Token
import token.TokenType

class NewLineAfterIfStatementAnalyzer : FormatRulesAnalyzers {
    override fun analyze(
        exToken: Token,
        currentToken: Token,
        currenString: String,
    ): Boolean = !currentToken.isOfType(TokenType.ENTER) && (exToken.value == "{" || exToken.value == "}")

    override fun giveExecutor(): FormatRulesExecutors = NewLineAfterIfStatementExecutor()

    override fun stillNecessaryToAddToken(): Boolean = false
}
