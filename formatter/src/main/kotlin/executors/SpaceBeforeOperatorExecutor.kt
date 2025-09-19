package executors

import token.Token

class SpaceBeforeOperatorExecutor : FormatRulesExecutors {
    override fun apply(
        exToken: Token,
        currentToken: Token,
        currentString: String,
    ): String = currentString + " " + currentToken.value
}
