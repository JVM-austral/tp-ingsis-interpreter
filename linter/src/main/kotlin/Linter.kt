import ast.Ast

interface Linter {

    fun lint(statements: List<Result<Ast>>): List<Error>
}
