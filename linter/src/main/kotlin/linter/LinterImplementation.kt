package linter

import analyzers.LinterAnalyzer
import ast.Ast
import ast.IfDeclaration
import error.LinterError

class LinterImplementation(
    private val rulesList: List<LinterAnalyzer>,
) : Linter {
    override fun lint(statements: List<Result<Ast>>): List<LinterError> {
        val errorsFound = mutableListOf<LinterError>()

        for (statement in statements) {
            statement.fold(
                onSuccess = {
                    val ast = statement.getOrNull()
                    if (ast != null) {
                        val error = detectRulesViolations(ast, rulesList)
                        errorsFound.addAll(error)
                    }
                },
                onFailure = {
                    throw Exception("Statement is not formatted correctly " + it.message)
                },
            )
        }

        return errorsFound
    }

    private fun detectRulesViolations(
        ast: Ast,
        listOfRules: List<LinterAnalyzer>,
    ): List<LinterError> {
        val listOfErrors = mutableListOf<LinterError>()
        var listOfIfErrors = listOf<LinterError>()
        var listOfElseErrors = listOf<LinterError>()

        if (ast is IfDeclaration) {
            listOfIfErrors = lint(ast.getOnSuccess())
            listOfElseErrors = lint(ast.getOnFailure())
        }

        for (rule in listOfRules) {
            val executed = rule.analyze(ast)
            if (executed.isPresent) {
                listOfErrors.add(executed.get())
            }
        }
        listOfErrors.addAll(listOfIfErrors)
        listOfErrors.addAll(listOfElseErrors)
        return listOfErrors
    }
}
