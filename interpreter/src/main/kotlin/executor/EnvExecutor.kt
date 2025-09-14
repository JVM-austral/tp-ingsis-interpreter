package executor

import ast.Ast
import interpreter.VariableInfo

class EnvExecutor : InterpreterExecutor {
    override fun execute(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>): Result<Ast> {
        TODO("Not yet implemented")
    }
}
