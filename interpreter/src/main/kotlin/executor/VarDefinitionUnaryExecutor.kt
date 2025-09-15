package executor

import ConditionMessageHandler
import ast.Ast
import ast.NumberLiteral
import ast.VarDefinition
import interpreter.VariableInfo

class VarDefinitionUnaryExecutor(private val conditionMessageHandler: ConditionMessageHandler) : InterpreterExecutor {
    override fun execute(statement: Result<ast.Ast>, heap: MutableMap<String, VariableInfo>, env: MutableMap<String, Ast>): Result<Ast> {
        val ast = statement.getOrNull() ?: return Result.failure(Exception("Invalid AST"))
        if (ast !is VarDefinition) {
            return Result.failure(Exception("AST is not a VarDefinition"))
        }
        val resultError = conditionMessageHandler.handleConditionMessage(statement, heap)
        if (resultError.isFailure) {
            return Result.failure(Exception(resultError.toString()))
        }

        val (variableName, variableType, variableValue) = setupAst(ast)

        setupHeap(heap, variableName, variableType, variableValue)

        return Result.success(ast)
    }

    private fun setupHeap(
        heap: MutableMap<String, VariableInfo>,
        variableName: String,
        variableType: String,
        variableValue: String,
    ) {
        heap[variableName] = VariableInfo(variableType, variableValue)
    }

    private fun setupAst(ast: Ast): Triple<String, String, String> {
        val variableName = ast.getListOfChildren()[0].getValue()
        val valueNode = ast.getListOfChildren()[1]
        val variableType = if (valueNode is NumberLiteral) "number" else "string"
        val variableValue = valueNode.getValue()
        return Triple(variableName, variableType, variableValue)
    }
}
