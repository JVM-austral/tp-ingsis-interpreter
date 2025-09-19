package executors

import token.Token

class NewLineAfterSemiColonExecutor : FormatRulesExecutors {
    override fun apply(
        exToken: Token,
        currentToken: Token,
        currentString: String,
    ): String {
        if (currentToken.isOfType(token.TokenType.WHITESPACE)) {
            return currentString + "\n"
        }
        return currentString + "\n" + currentToken.value
    }
}
