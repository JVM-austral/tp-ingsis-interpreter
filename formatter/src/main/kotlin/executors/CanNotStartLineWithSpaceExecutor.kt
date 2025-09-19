package executors

import token.Token

class CanNotStartLineWithSpaceExecutor : FormatRulesExecutors {
    override fun apply(
        exToken: Token,
        currentToken: Token,
        currentString: String,
    ): String = currentString
}
