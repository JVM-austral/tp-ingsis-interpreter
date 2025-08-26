package executors

import token.Token

class SpaceBeforeEqualsExecutor : FormatRulesExecutors {
    override fun apply(exToken: Token, currentToken: Token, currentString: String): String {
        return currentString + " ="
    }
}
