import analyzers.LinterAnalyzer
import ast.Ast
import error.LinterError


class LinterImplementation(private val rulesList: List<LinterAnalyzer>) : Linter {

    override fun lint(statements: List<Result<Ast>>): List<LinterError> {
        val errorsFound = mutableListOf<LinterError>()

        for (statement in statements) {
            statement.fold(
                onSuccess = {
                    val ast = statement.getOrNull()
                    if (ast != null) {
                        val error = doesNotMatchALintingRule(ast, rulesList)
                        errorsFound.addAll(error)
                    }
                },
                onFailure = {
                    throw Exception("Statement is not formatted correctly")
                },
            )
        }

        return errorsFound
    }

    private fun doesNotMatchALintingRule(ast: Ast, listOfRules: List<LinterAnalyzer>): List<LinterError> {

        val listOfErrors = mutableListOf<LinterError>()

        for (rule in listOfRules) {
            val executed = rule.analyze(ast)
            if (executed.isPresent) {
                listOfErrors.add(executed.get())
            }
        }
        return listOfErrors
    }
}
