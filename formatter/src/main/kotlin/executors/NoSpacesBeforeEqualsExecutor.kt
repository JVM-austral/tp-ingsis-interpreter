package executors

class NoSpacesBeforeEqualsExecutor : FormatRulesExecutors {
    override fun apply(exToken: token.Token, currentToken: token.Token, currentString: String): String {
        return currentString.dropLast(1) + currentToken.value
    }
}
