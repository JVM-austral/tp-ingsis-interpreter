package executors

class NecessarySpaceExecutor : FormatRulesExecutors {
    override fun apply(
        exToken: token.Token,
        currentToken: token.Token,
        currentString: String,
    ): String = currentString + " " + currentToken.value
}
