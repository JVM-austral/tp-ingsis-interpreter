package executors

import token.Token

class OnlyOneSpaceExecutor : FormatRulesExecutors {
    override fun apply(
        exToken: Token,
        currentToken: Token,
        currentString: String,
    ): String = currentString
}
