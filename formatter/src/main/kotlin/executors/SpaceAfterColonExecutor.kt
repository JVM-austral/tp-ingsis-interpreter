package executors

import token.Token

class SpaceAfterColonExecutor : FormatRulesExecutors {
    override fun apply(
        exToken: Token,
        currentToken: Token,
        currentString: String,
    ): String = currentString + " " + currentToken.value
}
