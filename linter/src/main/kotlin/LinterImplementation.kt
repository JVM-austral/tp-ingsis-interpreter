import analyzers.LinterAnalyzer
import ast.Ast
import java.util.*

class LinterImplementation(private val rulesList: List<LinterAnalyzer>) : Linter {

    override fun lint(statements: List<Result<Ast>>): List<Error> {
        val errorsFound = mutableListOf<Error>()

        for (statement in statements) {
            statement.fold(
                onSuccess = {
                    val ast = statement.getOrNull()
                    if (ast != null) {
                        val error = doesNotMatchALintingRule(ast, rulesList)
                        if (error.isPresent) {
                            errorsFound.add(error.get())
                        }
                    }
                },
                onFailure = {
                    throw Exception("Statement is not formatted correctly")
                },
            )
        }

        return errorsFound
    }

    private fun doesNotMatchALintingRule(ast: Ast, listOfRules: List<LinterAnalyzer>): Optional<Error> {
        for (rule in listOfRules) {
            val executed = rule.analyze(ast)
            if (executed.isPresent) {
                return executed
            }
        }
        return Optional.empty()
    }
}
