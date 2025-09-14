package executor

import ConditionMessageHandler
import ast.Ast
import ast.TypeDeclaration
import interpreter.VariableInfo

class TypeDeclarationExecutor(
    private val conditionMessageHandler: ConditionMessageHandler,
) : InterpreterExecutor {

    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>
    ): Result<Ast> {
        val ast = statement.getOrNull() ?: return errorResult("ast nulo")
        if (ast !is TypeDeclaration) return errorResult("No es una declaración de tipo")

        val variableName = ast.getValue()
        val variableType = ast.type

        val error = validarCondiciones(statement, heap)
        if (error != null) return errorResult(error)

        heap[variableName] = VariableInfo(type = variableType, value = "")
        return Result.success(ast)
    }

    private fun validarCondiciones(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
    ): String? {
        val resultError = conditionMessageHandler.handleConditionMessage(statement, heap)
        return if (resultError.isFailure) resultError.toString() else null
    }

    private fun errorResult(message: String?): Result<Ast> =
        Result.failure(Exception(message))
}
