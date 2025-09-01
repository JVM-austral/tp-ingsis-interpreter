package executor

import ast.Ast
import interpreter.VariableInfo

class FailInterpreterExecutor : InterpreterExecutor {
    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): Result<Ast> {
        return Result.failure(Exception("error en la estructura basica del ast "))
    }
}
