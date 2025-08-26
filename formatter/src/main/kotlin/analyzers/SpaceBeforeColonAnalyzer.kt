package analyzers

import executors.FormatRulesExecutors
import executors.SpaceBeforeColonExecutor
import token.Token
import token.TokenType

class SpaceBeforeColonAnalyzer : FormatRulesAnalyzers {
    override fun analyze(exToken: Token, currentToken: Token, currenString: String): Boolean {
        return currentToken.isOfType(TokenType.PUNCTUATION) && currentToken.value == ":" &&
            !exToken.isOfType(TokenType.WHITESPACE)
    }

    override fun giveExecutor(): FormatRulesExecutors {
        return SpaceBeforeColonExecutor()
    }
}
