package executors

import token.Token

class SpaceAfterOperatorExecutor : FormatRulesExecutors {
    override fun apply(exToken: Token, currentToken: Token, currentString: String): String {
        return currentString + " " + currentToken.value
    }
}
