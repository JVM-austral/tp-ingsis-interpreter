package executor
import ast.Ast

sealed class ParseResult {
    data class Success(
        val ast: Ast,
    ) : ParseResult()

    object Failure : ParseResult()
}
