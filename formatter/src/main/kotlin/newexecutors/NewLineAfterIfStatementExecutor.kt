package newexecutors

import executors.FormatRulesExecutors
import token.Token

class NewLineAfterIfStatementExecutor : FormatRulesExecutors {
    override fun apply(exToken: Token, currentToken: Token, currentString: String): String {
        if (currentToken.isOfType(token.TokenType.WHITESPACE)) {
            return currentString + "\n"
        }
        return currentString + "\n" + currentToken.value
    }
}
