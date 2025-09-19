package executors

class NoSpacesBeforeEqualsExecutor : FormatRulesExecutors {
    override fun apply(
        exToken: token.Token,
        currentToken: token.Token,
        currentString: String,
    ): String = currentString.dropLast(1) + currentToken.value
}
