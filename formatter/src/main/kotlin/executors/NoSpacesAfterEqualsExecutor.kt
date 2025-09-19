package executors

import token.Token

class NoSpacesAfterEqualsExecutor : FormatRulesExecutors {
    override fun apply(
        exToken: Token,
        currentToken: Token,
        currentString: String,
    ): String = currentString
}
