package executor

import ast.Ast
import interpreter.VariableInfo

class FailInterpreterExecutor(private val error: Throwable? = null) : InterpreterExecutor {
    override fun execute(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>, env: MutableMap<String, Ast>): Result<Ast> {
        return Result.failure(error ?: Exception("interpreter error"))
    }
}
