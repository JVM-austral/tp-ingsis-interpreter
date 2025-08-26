package executor

import ast.Ast
import interpreter.VariableInfo

class VarDeclarationWithAssigmentUnaryExecutor(private val heap: MutableMap<String, VariableInfo>) : InterpreterExecutor {

    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): Result<MutableMap<String, VariableInfo>> {
        return statement.fold(
            onSuccess = { ast ->
                val variable: String = ast.getChild()[0].getValue()
                val type: String = ast.getChild()[1].getValue()
                val value: String = ast.getChild()[2].getValue()

                heap[variable] = VariableInfo(type, value)
                Result.success(heap)
            },
            onFailure = {
                Result.failure(Exception("Error ejecutando VarDeclaration"))
            },
        )
    }
}
