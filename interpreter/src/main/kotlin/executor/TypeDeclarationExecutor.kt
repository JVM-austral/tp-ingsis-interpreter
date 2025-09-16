package executor

import ConditionMessageHandler
import ast.Ast
import ast.VarDeclaration
import interpreter.VariableInfo

class TypeDeclarationExecutor(
    private val conditionMessageHandler: ConditionMessageHandler,
) : InterpreterExecutor {

    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
        env: MutableMap<String, Ast>,
    ): Result<Ast> {
        val ast = statement.getOrNull() ?: return errorResult("ast nulo")

        if (ast !is VarDeclaration) return errorResult("No es una declaración de tipo")

        val variableName = ast.getListOfChildren()[0].getValue()
        val variableType = ast.getListOfChildren()[1].getValue()
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
