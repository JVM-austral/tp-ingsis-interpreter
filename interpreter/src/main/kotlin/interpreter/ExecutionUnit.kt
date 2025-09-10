package interpreter

import ast.Ast
import executor.InterpreterExecutor

data class ExecutionUnit(
    val executor: InterpreterExecutor,
    val statement: Result<Ast>,
    val message: String? = null,
) {
    val line: Int? get() = statement.getOrNull()?.getRow()
    val column: Int? get() = statement.getOrNull()?.getColumn()
}
