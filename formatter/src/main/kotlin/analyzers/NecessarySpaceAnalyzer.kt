package analyzers

class NecessarySpaceAnalyzer : FormatRulesAnalyzers {
    override fun analyze(
        exToken: token.Token,
        currentToken: token.Token,
        currenString: String,
    ): Boolean =
        currentToken.value != ";" &&
            exToken.value != ";" &&
            exToken.type.name != "WHITESPACE" &&
            currentToken.type.name != "WHITESPACE" &&
            exToken.type.name != "ENTER" &&
            currentToken.type.name != "ENTER"

    override fun giveExecutor(): executors.FormatRulesExecutors = executors.NecessarySpaceExecutor()

    override fun stillNecessaryToAddToken(): Boolean = false
}
