package analyzers

import executors.FormatRulesExecutors
import executors.OnlyOneSpaceExecutor
import token.Token
import token.TokenType

class OnlyOneSpaceAnalyzer : FormatRulesAnalyzers {
    override fun analyze(exToken: Token, currentToken: Token, currenString: String): Boolean {
        return exToken.isOfType(TokenType.WHITESPACE) and currentToken.isOfType(TokenType.WHITESPACE)
    }

    override fun giveExecutor(): FormatRulesExecutors {
        return OnlyOneSpaceExecutor()
    }
}
