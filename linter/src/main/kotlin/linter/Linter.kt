package linter

import ast.Ast
import error.LinterError

interface Linter {
    fun lint(statements: List<Result<Ast>>): List<LinterError>
}
