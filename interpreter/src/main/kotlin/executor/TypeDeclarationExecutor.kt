package executor

import ast.Ast
import ast.TypeDeclaration
import interpreter.VariableInfo

class TypeDeclarationExecutor : InterpreterExecutor {
    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): Result<MutableMap<String, VariableInfo>> {
        val ast = statement.getOrNull()
        if (ast is TypeDeclaration) {
            val variableName = ast.getValue()
            val variableType = ast.type
            if (heap.containsKey(variableName)) {
                return Result.failure(Exception("La variable '$variableName' ya está declarada"))
            }
            heap[variableName] = VariableInfo(type = variableType, value = "")
            return Result.success(heap)
        }
        return Result.failure(Exception("No es una declaración de tipo"))
    }
}
