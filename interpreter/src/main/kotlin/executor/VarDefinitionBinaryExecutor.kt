package executor

import ast.Ast
import interpreter.VariableInfo

class VarDefinitionBinaryExecutor(heap: MutableMap<String, VariableInfo>) : InterpreterExecutor {
    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): Result<MutableMap<String, VariableInfo>> {
        TODO("Not yet implemented")
    }
}
