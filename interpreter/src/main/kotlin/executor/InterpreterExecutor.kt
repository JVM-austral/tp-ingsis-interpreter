package executor

import ast.Ast
import interpreter.VariableInfo

interface InterpreterExecutor {
    fun execute(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): Result<Ast>
}
