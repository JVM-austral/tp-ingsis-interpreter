package analyzers

import executors.FormatRulesExecutors
import executors.SpaceAfterColonExecutor
import token.Token
import token.TokenType

class SpaceAfterColonAnalyzer : FormatRulesAnalyzers {
    override fun analyze(exToken: Token, currentToken: Token, currenString: String): Boolean {
        return !currentToken.isOfType(TokenType.WHITESPACE) && exToken.value == ":" &&
            exToken.isOfType(TokenType.PUNCTUATION)
    }

    override fun giveExecutor(): FormatRulesExecutors {
        return SpaceAfterColonExecutor()
    }
    override fun stillNecessaryToAddToken(): Boolean {
        return false
    }
}
