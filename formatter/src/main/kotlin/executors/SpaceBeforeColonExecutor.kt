package executors

import token.Token

class SpaceBeforeColonExecutor : FormatRulesExecutors {
    override fun apply(exToken: Token, currentToken: Token, currentString: String): String {
        return currentString + " :"
    }
}
